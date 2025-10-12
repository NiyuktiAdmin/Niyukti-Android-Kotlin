package com.example.niyuktikotlin.my_performance

import BaseActivity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R
import com.example.niyuktikotlin.models.CourseFolder
import com.example.niyuktikotlin.util.CourseFileAdapter

class MyPerformanceListingActivity : BaseActivity() {
    lateinit var recyclerView : RecyclerView
    lateinit var backBtn : ImageView
    lateinit var pageTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_performance_listing)

        recyclerView = findViewById(R.id.mpl_activity_rv)
        backBtn = findViewById(R.id.mpl_activity_back_btn)
        pageTitle = findViewById(R.id.mpl_page_title)

        pageTitle.text = intent.getStringExtra("pageTitle") ?: "My Performance"

        backBtn.setOnClickListener { finish() }
        initialiseRv()
    }

    fun initialiseRv() {
        val courseList = listOf(
            CourseFolder("Chapter 1", "3 subtopics", "id1"),
            CourseFolder("Chapter 2", "5 subtopics", "id1"),
            CourseFolder("Chapter 3", "2 subtopics", "id1"),
            CourseFolder("Chapter 4", "1 subtopic", "id1"),
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MyPerformanceCardAdapter(courseList)
    }
}