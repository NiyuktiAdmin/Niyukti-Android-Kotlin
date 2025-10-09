package com.example.niyuktikotlin.pdf_revision

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R
import com.example.niyuktikotlin.models.StudyMaterial
import com.example.niyuktikotlin.util.StudyMaterialAdapter

class PdfRevisionActivity : AppCompatActivity() {
    lateinit var recyclerView : RecyclerView
    lateinit var backBtn : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_revision)

        recyclerView = findViewById(R.id.pdf_revision_activity_rv)
        backBtn = findViewById(R.id.pdf_revision_activity_back_btn)

        backBtn.setOnClickListener { finish() }
        initialiseRv()
    }

    fun initialiseRv() {
        val materialList = listOf(
            StudyMaterial(R.drawable.sample_home_offer, "Learn about 19th-century religious and social re forms in India and Gujarat in a concise way.", "Revise now, Click Here"),
            StudyMaterial(R.drawable.offer1, "Explore the industrial revolution's effects on society and economy across the world.", "Revise now, Click Here"),
            StudyMaterial(R.drawable.course4, "Understand key concepts of Indian independence movements and their major contributors.", "Revise now, Click Here")
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = StudyMaterialAdapter(materialList)
    }
}