package com.example.niyuktikotlin.mock_test_list

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R
import com.example.niyuktikotlin.util.CourseBuyTagAdapter
import com.example.niyuktikotlin.util.CourseBuyAdapter
import com.example.niyuktikotlin.models.CourseModel

class MockTestListActivity : AppCompatActivity(), CourseBuyTagAdapter.OnTagSelectedListener {

    private lateinit var tagsRv: RecyclerView
    private lateinit var itemsRv: RecyclerView

    private lateinit var courseCategories: Map<String, List<CourseModel>>
    lateinit var backBtn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mock_test_list)

        itemsRv = findViewById(R.id.mock_test_list_activity_items_rv)
        tagsRv = findViewById(R.id.mock_test_list_activity_tags_rv)

        loadCourseData()
        setTagsRv()
        setItemsRv()

        backBtn = findViewById(R.id.mock_test_list_activity_menu_btn)
        backBtn.setOnClickListener {
            finish()
        }
    }

    private fun loadCourseData() {

        val ieltsCourses = listOf(
            CourseModel("id", "title", "desc", 100, 60, true, 40, R.drawable.course4, "helo")
        )

        val toeflCourses = ieltsCourses

        val upscCourses = ieltsCourses

        val jeeCourses = ieltsCourses

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

        val coursesAdapter = CourseBuyAdapter(coursesToDisplay, MockTestSubListActivity::class.java, "Mock Test")
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