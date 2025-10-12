package com.example.niyuktikotlin.mock_test_list

import BaseActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R
import com.example.niyuktikotlin.util.CourseBuyTagAdapter
import com.example.niyuktikotlin.util.CourseBuyAdapter
import com.example.niyuktikotlin.models.CourseModel

class MockTestListActivity : BaseActivity(), CourseBuyTagAdapter.OnTagSelectedListener {

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
            CourseModel("1", "IELTS Foundation", "Basic IELTS preparation course", 100, 60, true, 40, R.drawable.course4, "Learn IELTS basics"),
            CourseModel("2", "IELTS Advanced", "Advanced techniques for IELTS", 150, 90, false, 0, R.drawable.course4, "Master IELTS strategies"),
            CourseModel("3", "IELTS Practice Tests", "10 full-length mock tests", 120, 80, true, 50, R.drawable.course4, "Improve your band score")
        )

        val toeflCourses = listOf(
            CourseModel("4", "TOEFL Starter", "Introduction to TOEFL test pattern", 90, 50, true, 40, R.drawable.course4, "Start your TOEFL journey"),
            CourseModel("5", "TOEFL Vocabulary Booster", "Learn 500 essential TOEFL words", 110, 60, false, 0, R.drawable.course4, "Enhance vocabulary"),
            CourseModel("6", "TOEFL Mock Series", "5 full mock tests with analysis", 130, 85, true, 35, R.drawable.course4, "Test your skills")
        )

        val upscCourses = listOf(
            CourseModel("7", "UPSC Prelims", "Comprehensive course for Prelims", 200, 120, true, 50, R.drawable.course4, "Prepare for Prelims"),
            CourseModel("8", "UPSC Mains", "Answer writing practice & strategy", 250, 150, false, 0, R.drawable.course4, "Boost your Mains score"),
            CourseModel("9", "UPSC Test Series", "15 full-length tests", 300, 180, true, 40, R.drawable.course4, "Simulate real exam experience")
        )

        val jeeCourses = listOf(
            CourseModel("10", "JEE Physics", "Master mechanics and electromagnetism", 180, 100, true, 45, R.drawable.course4, "Crack JEE Physics"),
            CourseModel("11", "JEE Chemistry", "Organic, inorganic & physical chemistry", 170, 95, false, 0, R.drawable.course4, "Score high in Chemistry"),
            CourseModel("12", "JEE Mathematics", "Advanced problem-solving course", 190, 110, true, 30, R.drawable.course4, "Sharpen your math skills")
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