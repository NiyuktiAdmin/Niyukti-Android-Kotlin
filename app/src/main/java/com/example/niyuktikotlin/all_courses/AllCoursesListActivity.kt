package com.example.niyuktikotlin.all_courses

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R
import com.example.niyuktikotlin.models.CourseModel
import com.example.niyuktikotlin.util.CourseBuyAdapter
import com.example.niyuktikotlin.util.CourseBuyTagAdapter
import io.appwrite.Client
import io.appwrite.Query
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.Account
import io.appwrite.services.Databases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllCoursesListActivity : AppCompatActivity(), CourseBuyTagAdapter.OnTagSelectedListener {

    private lateinit var coursesRv: RecyclerView
    private lateinit var tagsRv: RecyclerView
    private lateinit var backBtn: ImageView

    private lateinit var client: Client
    private lateinit var databases: Databases
    private lateinit var account: Account
    private val scope = CoroutineScope(Dispatchers.IO)

    private var allAvailableCourses: List<CourseModel> = emptyList()

    private var filteredCourses: List<CourseModel> = emptyList()
    private lateinit var coursesAdapter: CourseBuyAdapter

    private var uniqueTags: List<String> = emptyList()
    private lateinit var tagsAdapter: CourseBuyTagAdapter

    private val ALL_TAG = "All"
    lateinit var pageTitle : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_courses_list)

        client = Client(this, getString(R.string.APPWRITE_ENDPOINT)).setProject(getString(R.string.APPWRITE_PROJECT_ID))
        databases = Databases(client)
        account = Account(client)

        initialiseVariables()

        coursesAdapter = CourseBuyAdapter(filteredCourses, CourseFoldersActivity::class.java, CourseBuyAdapter.ITEM_NAME)
        coursesRv.layoutManager = LinearLayoutManager(this)
        coursesRv.adapter = coursesAdapter

        fetchAndSetupData()
    }

    private fun initialiseVariables() {
        coursesRv = findViewById(R.id.all_courses_list_activity_items_rv)
        tagsRv = findViewById(R.id.all_courses_list_activity_tags_rv)
        backBtn = findViewById(R.id.all_courses_list_activity_back_btn)
        pageTitle = findViewById(R.id.all_courses_list_activity_page_title)

        backBtn.setOnClickListener {
            finish()
        }
        val titleFromIntent = intent.getStringExtra("pageTitle")
        pageTitle.text = titleFromIntent ?: "All Courses"
    }

    private fun fetchAndSetupData() {
        scope.launch {
            try {
                val userId = fetchCurrentUserId()

                val fetchedCourses = fetchAllCoursesForDisplay(userId)
                allAvailableCourses = fetchedCourses

                val tagsSet = mutableSetOf<String>()
                allAvailableCourses.forEach { course ->
                    tagsSet.addAll(course.tags)
                }
                uniqueTags = listOf(ALL_TAG) + tagsSet.sorted()

                withContext(Dispatchers.Main) {
                    setupTagsRv()

                    filteredCourses = allAvailableCourses
                    updateCoursesRv(filteredCourses)
                }

            } catch (e: AppwriteException) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AllCoursesListActivity, "Failed to load data: ${e.message}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AllCoursesListActivity, "An unexpected error occurred.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private suspend fun fetchCurrentUserId(): String? {
        return try {
            account.get().id
        } catch (e: AppwriteException) {
            null
        }
    }

    private suspend fun fetchAllCoursesForDisplay(userId: String?): List<CourseModel> {
        val queries = mutableListOf<String>()

        queries.add(Query.limit(100))
        queries.add(Query.orderDesc("\$createdAt"))

        val response = databases.listDocuments(
            databaseId = getString(R.string.DATABASE_ID),
            collectionId = getString(R.string.COURSES_COLLECTION_ID),
            queries = queries
        )

        val courses = mutableListOf<CourseModel>()

        response.documents.forEach { document ->
            val data = document.data

            if (userId != null) {
                val purchasedByList = data["purchasedBy"] as? List<String> ?: emptyList()
                if (purchasedByList.contains(userId)) {
                    return@forEach
                }
            }

            val finalPrice = (data["price"] as? Number)?.toInt() ?: 0
            val actualPrice = (data["PrevPrice"] as? Number)?.toInt() ?: 0
            val title = data["title"] as? String ?: "No Title"
            val tagsList = data["tags"] as? List<String> ?: emptyList()
            val desc = data["description"] as? String ?: "No Description"
            val isActive = data["isActive"] as? Boolean ?: false
            val discountPercent = if (actualPrice > 0) ((actualPrice - finalPrice) * 100 / actualPrice) else 0
            val syllabusIds = data["syllabusId"] as? List<String> ?: emptyList()
            val testsIds = data["testsId"] as? List<String> ?: emptyList()
            val plansIds = data["plansId"] as? List<String> ?: emptyList()

            courses.add(
                CourseModel(
                    document.id,
                    title, desc, actualPrice, finalPrice, isActive, discountPercent,
                    imageResId = R.drawable.course4,
                    category = tagsList.firstOrNull()?.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } ?: "General",
                    tagsList, syllabusIds, testsIds, plansIds
                )
            )
        }
        return courses
    }

    private fun setupTagsRv() {
        tagsRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        tagsAdapter = CourseBuyTagAdapter(uniqueTags, this)
        tagsRv.adapter = tagsAdapter

        if (uniqueTags.isNotEmpty() && uniqueTags.first() == ALL_TAG) {
            onTagSelected(ALL_TAG)
        }
    }

    override fun onTagSelected(tagName: String) {
        filteredCourses = if (tagName == ALL_TAG) {
            allAvailableCourses
        } else {
            allAvailableCourses.filter { it.tags.contains(tagName) }
        }

        updateCoursesRv(filteredCourses)
    }

    private fun updateCoursesRv(list: List<CourseModel>) {
        coursesAdapter = CourseBuyAdapter(list, CourseFoldersActivity::class.java, CourseBuyAdapter.ITEM_NAME)
        coursesRv.adapter = coursesAdapter
    }
}