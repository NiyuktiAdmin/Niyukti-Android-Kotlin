package com.example.niyuktikotlin.test_info

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.niyuktikotlin.R

class GeneralTestInstructionsActivity : AppCompatActivity() {
    var startTestBtn: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general_test_instructions)

        startTestBtn = findViewById<TextView>(R.id.general_test_instruction_start_btn)
        startTestBtn?.setOnClickListener {
            val intent = Intent(this, TestConfigurationActivity::class.java)
            startActivity(intent)
        }
    }
}