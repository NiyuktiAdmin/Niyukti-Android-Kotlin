package com.example.niyuktikotlin.mock_test_list

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R
import com.example.niyuktikotlin.course_resources.CourseResourcesActivity
import com.example.niyuktikotlin.models.CourseFolder
import com.example.niyuktikotlin.util.CourseFileAdapter

class MockTestSubListActivity : AppCompatActivity() {
    private lateinit var adapter: CourseFileAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var backBtn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mock_test_sub_list)

        recyclerView = findViewById(R.id.mock_test_sub_list_rv)
        backBtn = findViewById(R.id.mock_test_sublist_activity_menu_btn)
        backBtn.setOnClickListener {
            finish()
        }
        setItemsRv()
    }

    private fun setItemsRv() {
        val recentlyAddedLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = recentlyAddedLayoutManager
        val list = listOf(
            CourseFolder("test1", "this is test 1", "id"),
            CourseFolder("test1", "this is test 1", "id")
        )
        adapter = CourseFileAdapter(
            list,
            CourseResourcesActivity::class.java,
            "@undone",
            false
        )
        recyclerView.adapter = adapter
    }
}