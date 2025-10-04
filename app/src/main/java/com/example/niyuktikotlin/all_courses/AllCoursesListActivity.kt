package com.example.niyuktikotlin.all_courses

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R

import com.example.niyuktikotlin.util.CourseBuyAdapter
import com.example.niyuktikotlin.util.CourseBuyTagAdapter
import com.example.niyuktikotlin.util.CourseModel


open class AllCoursesListActivity : AppCompatActivity(), CourseBuyTagAdapter.OnTagSelectedListener { // IMPLEMENT Listener

    private lateinit var tagsRv: RecyclerView
    private lateinit var itemsRv: RecyclerView

    private lateinit var courseCategories: Map<String, List<CourseModel>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_courses_list)

        itemsRv = findViewById(R.id.all_courses_list_activity_items_rv)
        tagsRv = findViewById(R.id.all_courses_list_activity_tags_rv)

        loadCourseData()
        setTagsRv()
        setItemsRv()
    }

    private fun loadCourseData() {

        val ieltsCourses = listOf(
            CourseModel("IELTS Speaking Mastery: Band 9 Strategies", 2499, 1249, 50, R.drawable.sample_home_offer, "IELTS"),
            CourseModel("IELTS Writing Task 1 & 2 Intensive", 2199, 1099, 50, R.drawable.offer1, "IELTS"),
            CourseModel("IELTS Grammar Booster for High Scores", 1599, 799, 50, R.drawable.offer2, "IELTS"),
            CourseModel("Complete IELTS Preparation Bundle", 2999, 1499, 50, R.drawable.offer3, "IELTS")
        )

        val toeflCourses = listOf(
            CourseModel("TOEFL Vocabulary Builder", 900, 540, 40, R.drawable.offer2, "TOEFL"),
            CourseModel("TOEFL Reading Practice", 1500, 750, 50, R.drawable.offer3, "TOEFL")
        )

        val upscCourses = listOf(
            CourseModel("UPSC GS Foundation 2024", 5000, 3000, 40, R.drawable.offer1, "UPSC"),
            CourseModel("UPSC History Optional", 3000, 1500, 50, R.drawable.offer2, "UPSC"),
            CourseModel("UPSC Current Affairs", 1000, 1000, 0, R.drawable.offer3, "UPSC")
        )

        val jeeCourses = listOf(
            CourseModel("JEE Physics Crash Course", 4000, 2400, 40, R.drawable.sample_home_offer, "JEE"),
        )

        courseCategories = linkedMapOf(
            "All" to ieltsCourses + toeflCourses + upscCourses + jeeCourses,
            "IELTS" to ieltsCourses,
            "TOEFL" to toeflCourses,
            "UPSC" to upscCourses,
            "JEE" to jeeCourses
        )
    }

    private fun setItemsRv(courses: List<CourseModel>? = null) {
        val coursesToDisplay = courses ?: courseCategories["All"] ?: emptyList()

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        itemsRv.layoutManager = layoutManager

        val coursesAdapter = CourseBuyAdapter(coursesToDisplay)
        itemsRv.adapter = coursesAdapter
        coursesAdapter.notifyDataSetChanged()
    }

    private fun setTagsRv() {
        val tags = courseCategories.keys.toList()

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        tagsRv.layoutManager = layoutManager

        val tagsAdapter = CourseBuyTagAdapter(tags, this)
        tagsRv.adapter = tagsAdapter
    }

    override fun onTagSelected(tagName: String) {
        val selectedCourses = courseCategories[tagName] ?: emptyList()
        setItemsRv(selectedCourses)
    }
}