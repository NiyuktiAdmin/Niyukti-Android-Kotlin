package com.example.niyuktikotlin.free_material_tests

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R
import com.example.niyuktikotlin.course_resources.CourseResourcesActivity
import com.example.niyuktikotlin.models.CourseFolder
import com.example.niyuktikotlin.models.StudyMaterial
import com.example.niyuktikotlin.pdf_revision.PdfListActivity
import com.example.niyuktikotlin.util.CourseFileAdapter

class FreeMaterialTestActivity : AppCompatActivity() {
    lateinit var recyclerView : RecyclerView
    lateinit var backBtn : ImageView

    lateinit var testBtn: TextView
    lateinit var materialsBtn: TextView
    lateinit var planBtn: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_free_material_test)

        initialiseBtn()

        backBtn.setOnClickListener { finish() }

        initialiseRv()
    }

    private fun initialiseBtn() {
        recyclerView = findViewById(R.id.free_mt_activity_rv)
        backBtn = findViewById(R.id.free_mt_activity_back_btn)

        testBtn = findViewById(R.id.free_mt_activity_test_btn)
        testBtn.setOnClickListener {
            updateSelectedTab(testBtn)
        }
        materialsBtn = findViewById(R.id.free_mt_activity_materials_btn)
        materialsBtn.setOnClickListener {
            updateSelectedTab(materialsBtn)
        }
        planBtn = findViewById(R.id.free_mt_activity_plan_btn)
        planBtn.setOnClickListener {
            updateSelectedTab(planBtn)
        }
        updateSelectedTab(materialsBtn)
    }

    private fun updateSelectedTab(selectedBtn: TextView) {
        materialsBtn.background = null
        testBtn.background = null
        planBtn.background = null

        selectedBtn.setBackgroundResource(R.drawable.bgrd_bottom_border)
    }

    private fun initialiseRv() {
        val courseList = listOf(
            CourseFolder("Hindi Papers", "3 subtopics", "id1"),
            CourseFolder("Civics Papers", "5 subtopics", "id1"),
            CourseFolder("Politics Papers", "2 subtopics", "id1"),
            CourseFolder("General Knowledge", "1 subtopic", "id1"),
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CourseFileAdapter(courseList, CourseResourcesActivity::class.java, "Free Resources", true)
    }
}