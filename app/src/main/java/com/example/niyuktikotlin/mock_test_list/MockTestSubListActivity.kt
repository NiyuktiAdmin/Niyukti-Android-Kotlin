package com.example.niyuktikotlin.mock_test_list

import BaseActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.PaymentHandlerActivity
import com.example.niyuktikotlin.R
import com.example.niyuktikotlin.course_resources.CourseResourcesActivity
import com.example.niyuktikotlin.models.CourseFolder
import com.example.niyuktikotlin.util.CourseFileAdapter

class MockTestSubListActivity : PaymentHandlerActivity() {
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

    override fun handlePaymentSuccess(razorpayPaymentID: String?) {
//        TODO("Not yet implemented")
    }

    override fun handlePaymentError(code: Int, response: String?) {
//        TODO("Not yet implemented")
    }

    private fun setItemsRv() {
        val recentlyAddedLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = recentlyAddedLayoutManager
        val list = listOf(
            CourseFolder("Mathematics 101", "Introduction to basic algebra and geometry concepts", "course_001"),
            CourseFolder("Physics Fundamentals", "Covers Newtonian mechanics and basic thermodynamics", "course_002"),
            CourseFolder("World History", "Explores major historical events from ancient to modern times", "course_003"),
            CourseFolder("Computer Science Basics", "Introduction to programming, algorithms, and data structures", "course_004"),
            CourseFolder("Creative Writing", "Develop storytelling and narrative writing techniques", "course_005")
        )
        adapter = CourseFileAdapter(list, CourseResourcesActivity::class.java, "Mock Test", false)
        recyclerView.adapter = adapter
    }
}