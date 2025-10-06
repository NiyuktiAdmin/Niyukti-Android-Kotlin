package com.example.niyuktikotlin.subject_wise_course

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R
import com.example.niyuktikotlin.util.CourseBuyAdapter
import com.example.niyuktikotlin.util.CourseBuyTagAdapter
import com.example.niyuktikotlin.models.CourseModel

class SubjectWiseCourseListActivity : AppCompatActivity(), CourseBuyTagAdapter.OnTagSelectedListener {
    private lateinit var tagsRv: RecyclerView
    private lateinit var itemsRv: RecyclerView
    lateinit var backBtn: ImageView

    private lateinit var courseCategories: Map<String, List<CourseModel>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject_wise_course_list)
        loadCourseData()

        itemsRv = findViewById(R.id.sub_courses_list_activity_items_rv)
        tagsRv = findViewById(R.id.sub_courses_list_activity_tags_rv)
        backBtn = findViewById(R.id.sub_courses_list_activity_menu_btn)

        backBtn.setOnClickListener {
            finish()
        }

        setTagsRv()
        setItemsRv()
    }

    private fun loadCourseData() {

        val englishCourses = listOf(
            CourseModel("English Grammar Essentials", 2499, 1249, 50, R.drawable.sample_home_offer, "English"),
            CourseModel("Advanced Writing Skills", 2199, 1099, 50, R.drawable.offer1, "English"),
            CourseModel("Reading Comprehension Mastery", 1599, 799, 50, R.drawable.offer2, "English"),
            CourseModel("Vocabulary Builder", 2999, 1499, 50, R.drawable.offer3, "English")
        )

        val hindiCourses = listOf(
            CourseModel("Hindi Vyakaran (Grammar)", 900, 540, 40, R.drawable.offer2, "Hindi"),
            CourseModel("Hindi Kavita Path", 1500, 750, 50, R.drawable.offer3, "Hindi"),
            CourseModel("Hindi Lekhan Kala", 1800, 900, 50, R.drawable.offer1, "Hindi"),
            CourseModel("Hindi Sahitya Vishleshan", 2200, 1100, 50, R.drawable.sample_home_offer, "Hindi")
        )

        val mathCourses = listOf(
            CourseModel("Algebra Fundamentals", 5000, 3000, 40, R.drawable.offer1, "Maths"),
            CourseModel("Geometry and Mensuration", 3000, 1500, 50, R.drawable.offer2, "Maths"),
            CourseModel("Trigonometry Crash Course", 2000, 1000, 50, R.drawable.offer3, "Maths"),
            CourseModel("Calculus Made Easy", 1000, 800, 20, R.drawable.sample_home_offer, "Maths")
        )

        val scienceCourses = listOf(
            CourseModel("Physics Fundamentals", 4000, 2400, 40, R.drawable.sample_home_offer, "Science"),
            CourseModel("Chemistry Concepts Simplified", 3500, 2100, 40, R.drawable.offer1, "Science"),
            CourseModel("Biology for Beginners", 3800, 1900, 50, R.drawable.offer2, "Science"),
            CourseModel("Environmental Science Basics", 2500, 1500, 40, R.drawable.offer3, "Science")
        )

        courseCategories = linkedMapOf(
            "All" to englishCourses + hindiCourses + mathCourses + scienceCourses,
            "English" to englishCourses,
            "Hindi" to hindiCourses,
            "Maths" to mathCourses,
            "Science" to scienceCourses
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