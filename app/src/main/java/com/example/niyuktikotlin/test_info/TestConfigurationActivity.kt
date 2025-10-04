package com.example.niyuktikotlin.test_info

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.niyuktikotlin.R
import com.example.niyuktikotlin.test_conducting.TestMainActivity

class TestConfigurationActivity : AppCompatActivity() {

    private lateinit var startTestBtn: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_configuration)

        startTestBtn = findViewById(R.id.test_configuration_start_btn)
        val spinner: Spinner = findViewById(R.id.category_spinner)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedCategory = parent.getItemAtPosition(position).toString()
                // Handle category selection here
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Optional: handle case where nothing is selected
            }
        }

        startTestBtn.setOnClickListener {
            val intent = Intent(this, TestMainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}