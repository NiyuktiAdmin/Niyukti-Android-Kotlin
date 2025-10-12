package com.example.niyuktikotlin.all_courses

import BaseActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R
import com.example.niyuktikotlin.models.CourseFolder
import com.example.niyuktikotlin.models.CourseModel
import com.example.niyuktikotlin.pdf_revision.PdfListActivity
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

class CourseFoldersActivity : BaseActivity() {
    // Existing variables
    private lateinit var recyclerView : RecyclerView
    private lateinit var backBtn : ImageView
    private lateinit var testBtn: TextView
    private lateinit var materialsBtn: TextView
    private lateinit var planBtn: TextView
    private lateinit var pageTitle: TextView
    private lateinit var currentCourse: CourseModel

    private lateinit var client: Client
    private lateinit var databases: Databases
    private val scope = CoroutineScope(Dispatchers.IO)

    private var currentFoldersList: List<CourseFolder> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_folders)

        client = Client(this, getString(R.string.APPWRITE_ENDPOINT)).setProject(getString(R.string.APPWRITE_PROJECT_ID))
        databases = Databases(client)

        initialise()

        backBtn.setOnClickListener { finish() }

        fetchSyllabusData()
    }

    private fun initialise() {
        recyclerView = findViewById(R.id.course_folder_activity_rv)
        backBtn = findViewById(R.id.course_folder_activity_back_btn)

        testBtn = findViewById(R.id.course_folder_activity_test_btn)
        materialsBtn = findViewById(R.id.course_folder_activity_materials_btn)
        planBtn = findViewById(R.id.course_folder_activity_plan_btn)

        pageTitle = findViewById(R.id.course_folder_activity_title)

        currentCourse = intent.getParcelableExtra<CourseModel>("pageItem")!!

        materialsBtn.setOnClickListener {
            fetchSyllabusData()
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

        val titleFromIntent = intent.getStringExtra("pageTitle")
        pageTitle.text = titleFromIntent ?: "Courses"
    }

    private fun updateSelectedTab(selectedBtn: TextView) {
        materialsBtn.background = null
        testBtn.background = null
        planBtn.background = null

        selectedBtn.setBackgroundResource(R.drawable.bgrd_bottom_border)
    }

    private fun updateRecyclerView(list: List<CourseFolder>) {
        if (list.isEmpty()) {
            val unavailableAdapter = ResourceUnavailableAdapter(
                pageText = "No syllabus items available.",
                btnText = null
            )
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = unavailableAdapter
            return
        }

        val destinationActivity = CourseFolderItemsActivity::class.java

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CourseFileAdapter(list, destinationActivity, pageTitle.text.toString(), true)
    }


    private fun fetchSyllabusData() {
        if (currentCourse.syllabusIds.isEmpty()) {
            Toast.makeText(this, "No syllabus items available for this course.", Toast.LENGTH_SHORT).show()
            updateRecyclerView(emptyList())
            return
        }

        scope.launch {
            try {
                val queries = listOf(
                    Query.equal("\$id", currentCourse.syllabusIds)
                )

                val response = databases.listDocuments(
                    databaseId = getString(R.string.DATABASE_ID),
                    collectionId = getString(R.string.SYLLABUS_COLLECTION_ID),
                    queries = queries
                )

                val syllabusFolders = mutableListOf<CourseFolder>()

                response.documents.forEach { document ->
                    val data = document.data
                    val name = data["name"] as? String ?: "No Title"
                    val subHeading = data["subHeading"] as? String ?: "No Description"

                    syllabusFolders.add(
                        CourseFolder(
                            title = name,
                            desc = subHeading,
                            id = document.id
                        )
                    )
                }

                currentFoldersList = syllabusFolders

                withContext(Dispatchers.Main) {
                    updateRecyclerView(currentFoldersList)
                }

            } catch (e: AppwriteException) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CourseFoldersActivity, "Failed to load syllabus: ${e.message}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CourseFoldersActivity, "An unexpected error occurred while fetching syllabus.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}