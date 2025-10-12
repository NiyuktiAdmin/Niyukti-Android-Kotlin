package com.example.niyuktikotlin.course_resources

import BaseActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R

class CourseResourcesActivity : BaseActivity() {

    private lateinit var resourcesRv: RecyclerView
    private lateinit var testsRv: RecyclerView
    private lateinit var resourceAdapter: CourseResourceAdapter
    private lateinit var testsAdapter: CoursesTestsAdapter
    private lateinit var backBtn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_resources)

        resourcesRv = findViewById(R.id.course_resource_rv)
        testsRv = findViewById(R.id.course_resources_test_rv)
        backBtn = findViewById(R.id.course_resources_test_back_btn)

        backBtn.setOnClickListener {
            finish()
        }
        setRecyclerViews()
    }

    private fun setRecyclerViews() {
        // Horizontal layout for tests
        val horizontalLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        testsRv.layoutManager = horizontalLayoutManager

        // Vertical layout for resources
        val verticalLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        resourcesRv.layoutManager = verticalLayoutManager

        // Initialize adapters
        testsAdapter = CoursesTestsAdapter()
        testsRv.adapter = testsAdapter

        resourceAdapter = CourseResourceAdapter()
        resourcesRv.adapter = resourceAdapter
    }
}