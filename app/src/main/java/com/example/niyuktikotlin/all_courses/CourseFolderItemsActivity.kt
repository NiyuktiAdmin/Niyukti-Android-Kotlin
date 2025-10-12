package com.example.niyuktikotlin.all_courses

import BaseActivity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R
import com.example.niyuktikotlin.course_resources.CourseResourcesActivity
import com.example.niyuktikotlin.models.CourseFolder
import com.example.niyuktikotlin.util.CourseFileAdapter
import com.example.niyuktikotlin.util.ResourceUnavailableAdapter
import io.appwrite.Client
import io.appwrite.Query
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.Databases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CourseFolderItemsActivity : BaseActivity() {
    private lateinit var recyclerView : RecyclerView
    private lateinit var backBtn : ImageView
    private lateinit var testBtn: TextView
    private lateinit var materialsBtn: TextView
    private lateinit var planBtn: TextView
    private lateinit var pageTitle: TextView

    private lateinit var client: Client
    private lateinit var databases: Databases
    private val scope = CoroutineScope(Dispatchers.IO)
    private var syllabusItemId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_folder_items)

        client = Client(this, getString(R.string.APPWRITE_ENDPOINT)).setProject(getString(R.string.APPWRITE_PROJECT_ID))
        databases = Databases(client)

        initialiseBtn()

        backBtn.setOnClickListener { finish() }

        materialsBtn.performClick()
    }

    private fun initialiseBtn() {
        recyclerView = findViewById(R.id.course_fi_activity_rv)
        backBtn = findViewById(R.id.course_fi_activity_back_btn)

        testBtn = findViewById(R.id.course_fi_activity_test_btn)
        materialsBtn = findViewById(R.id.course_fi_activity_materials_btn)
        planBtn = findViewById(R.id.course_fi_activity_plan_btn)

        pageTitle = findViewById(R.id.course_fi_activity_title)

        syllabusItemId = intent.getStringExtra("courseId") ?: ""
        pageTitle.text = intent.getStringExtra("pageTitle") ?: "Course Material"

        Log.d("CourseFolderItemsActivity", "syllabusItemId: $syllabusItemId")

        materialsBtn.setOnClickListener {
            fetchAndDisplayTopics()
            updateSelectedTab(materialsBtn)
        }
        testBtn.setOnClickListener {
            updateRecyclerView(emptyList())
            updateSelectedTab(testBtn)
        }
        planBtn.setOnClickListener {
            updateRecyclerView(emptyList())
            updateSelectedTab(planBtn)
        }
    }

    private fun updateSelectedTab(selectedBtn: TextView) {
        materialsBtn.background = null
        testBtn.background = null
        planBtn.background = null

        selectedBtn.setBackgroundResource(R.drawable.bgrd_bottom_border)
    }

    private fun fetchAndDisplayTopics() {
        if (syllabusItemId.isEmpty()) {
            updateRecyclerView(emptyList())
            return
        }

        scope.launch {
            try {
                val syllabusDoc = databases.getDocument(
                    databaseId = getString(R.string.DATABASE_ID),
                    collectionId = getString(R.string.SYLLABUS_COLLECTION_ID),
                    documentId = syllabusItemId
                )

                val topicIds = syllabusDoc.data["topicsId"] as? List<String> ?: emptyList()

                if (topicIds.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        updateRecyclerView(emptyList())
                    }
                    return@launch
                }

                val topicQueries = listOf(
                    Query.equal("\$id", topicIds)
                )

                val topicsResponse = databases.listDocuments(
                    databaseId = getString(R.string.DATABASE_ID),
                    collectionId = getString(R.string.TOPICS_COLLECTION_ID),
                    queries = topicQueries
                )

                val topicFolders = mutableListOf<CourseFolder>()

                topicsResponse.documents.forEach { document ->
                    val data = document.data
                    val name = data["name"] as? String ?: "No Title"
                    val subHeading = data["subHeading"] as? String ?: "No Description"

                    topicFolders.add(
                        CourseFolder(
                            title = name,
                            desc = subHeading,
                            id = document.id
                        )
                    )
                }

                withContext(Dispatchers.Main) {
                    updateRecyclerView(topicFolders)
                }

            } catch (e: AppwriteException) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CourseFolderItemsActivity, "Failed to load topics: ${e.message}", Toast.LENGTH_LONG).show()
                    updateRecyclerView(emptyList())
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CourseFolderItemsActivity, "An unexpected error occurred: ${e.message}", Toast.LENGTH_LONG).show()
                    updateRecyclerView(emptyList())
                }
            }
        }
    }

    private fun updateRecyclerView(list: List<CourseFolder>) {
        if (list.isEmpty()) {
            val unavailableAdapter = ResourceUnavailableAdapter(
                pageText = "No topics available.",
                btnText = null
            )
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = unavailableAdapter
            return
        }

        val destinationActivity = CourseResourcesActivity::class.java

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CourseFileAdapter(list, destinationActivity, pageTitle.text.toString(), false)
    }
}