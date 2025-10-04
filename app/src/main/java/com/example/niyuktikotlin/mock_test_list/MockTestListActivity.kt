package com.example.niyuktikotlin.mock_test_list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R
import com.example.niyuktikotlin.home_page.HomePageConstableAdapter
import com.example.niyuktikotlin.mock_test_list.MockTestListTagsAdapter

class MockTestListActivity : AppCompatActivity() {

    private lateinit var tagsRv: RecyclerView
    private lateinit var itemsRv: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mock_test_list)

        itemsRv = findViewById(R.id.mock_test_list_activity_items_rv)
        tagsRv = findViewById(R.id.mock_test_list_activity_tags_rv)

        setItemsRv()
        setTagsRv()
    }

    private fun setItemsRv() {
        val recentlyAddedLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        itemsRv.layoutManager = recentlyAddedLayoutManager

        val recentlyAddedAdapter = HomePageConstableAdapter()
        itemsRv.adapter = recentlyAddedAdapter
    }

    protected fun setTagsRv() {
        val recentlyAddedLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        tagsRv.layoutManager = recentlyAddedLayoutManager

        val tagsAdapter = MockTestListTagsAdapter()
        tagsRv.adapter = tagsAdapter
    }
}