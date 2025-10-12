package com.example.niyuktikotlin.how_to_use

import BaseActivity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R
import com.example.niyuktikotlin.course_resources.CourseResourcesActivity
import com.example.niyuktikotlin.models.CourseFolder
import com.example.niyuktikotlin.pdf_revision.PdfListActivity
import com.example.niyuktikotlin.util.CourseFileAdapter

class HowToUseActivity : BaseActivity() {
    lateinit var recyclerView : RecyclerView
    lateinit var backBtn : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_how_to_use)

        recyclerView = findViewById(R.id.usage_activity_rv)
        backBtn = findViewById(R.id.usage_activity_back_btn)

        backBtn.setOnClickListener { finish() }
        initialiseRv()
    }

    fun initialiseRv() {
        val courseList = listOf(
            CourseFolder("Hindi Papers", "3 subtopics", "id1"),
            CourseFolder("Civics Papers", "5 subtopics", "id1"),
            CourseFolder("Politics Papers", "2 subtopics", "id1"),
            CourseFolder("General Knowledge", "1 subtopic", "id1"),
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CourseFileAdapter(courseList, CourseResourcesActivity::class.java, "@undone", false)
    }
}