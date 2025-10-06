package com.example.niyuktikotlin.menu_pages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.niyuktikotlin.R

class AboutUsActivity : AppCompatActivity() {
    lateinit var backBtn: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)

        backBtn = findViewById(R.id.about_us_back)
        backBtn.setOnClickListener {
            finish()
        }
    }
}