package com.example.niyuktikotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.util.HallOfFameAdapter
import com.example.niyuktikotlin.util.StudentReviewAdapter
import com.example.niyuktikotlin.menu_fragment.FragmentMainMenu
import com.example.niyuktikotlin.models.HallOfFameModel
import com.example.niyuktikotlin.models.StudentReviewModel
import com.example.niyuktikotlin.util.SimpleImageAdapter

class MainActivity : AppCompatActivity() {
    lateinit var navigationMenuBtn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find all RecyclerViews from the layout
        val rv3Item = findViewById<RecyclerView>(R.id.main_activity_3_item_rv)
        val rvUpcoming = findViewById<RecyclerView>(R.id.main_activity_upcoming_rv)
        val rvOffer = findViewById<RecyclerView>(R.id.main_activity_offer_rv)
        val rvSelectionWall = findViewById<RecyclerView>(R.id.main_activity_selection_wall_rv)
        val rvStudentReview = findViewById<RecyclerView>(R.id.main_activity_student_review_rv)

        // Setup for RecyclerViews using HomePageOfferAdapter
        setupTaskRecyclerView(rv3Item)
        setupOfferRecyclerView(rvUpcoming)
        setupOfferRecyclerView(rvOffer)

        // Setup for the "Niyukti Selection Wall" RecyclerView
        setupSelectionWallRecyclerView(rvSelectionWall)

        // Setup for the "Student Review" RecyclerView
        setupStudentReviewRecyclerView(rvStudentReview)
        setNavigationMenuBtn()
    }

    private fun setNavigationMenuBtn() {
        navigationMenuBtn = findViewById(R.id.home_activity_menu_btn)
        navigationMenuBtn.setOnClickListener {
            val mainMenuFragment = FragmentMainMenu()
            mainMenuFragment.show(supportFragmentManager, FragmentMainMenu.TAG)
        }
    }

    private fun setupOfferRecyclerView(recyclerView: RecyclerView) {
        val offerImages = listOf(
            R.drawable.offer1,
            R.drawable.sample_home_offer,
            R.drawable.offer2,
            R.drawable.course4
        )
        val adapter = SimpleImageAdapter(offerImages)
        setupHorizontalRecyclerView(recyclerView, adapter)
    }

    private fun setupTaskRecyclerView(recyclerView: RecyclerView) {
        val offerImages = listOf(
            R.drawable.temp_active_course,
            R.drawable.test_performance,
            R.drawable.ref_earning_temp
        )
        val adapter = SimpleImageAdapter(offerImages)
        setupHorizontalRecyclerView(recyclerView, adapter)
    }

    private fun setupSelectionWallRecyclerView(recyclerView: RecyclerView) {
        val hallOfFameList = listOf(
            HallOfFameModel(
                userName = "Nannaraj Rathod",
                achievement = "PSI 2025",
                testimonial = "Thanks to Niyuktisir I cleared CCE Exam with flying colors.",
                profileImageResId = R.drawable.sample_profile_1
            ),
            HallOfFameModel(
                userName = "Priya Sharma",
                achievement = "Clerk 2024",
                testimonial = "The course structure was perfect for my preparation strategy.",
                profileImageResId = R.drawable.sample_profile_1
            ),
            HallOfFameModel(
                userName = "Amit Patel",
                achievement = "GPSC Class 1/2",
                testimonial = "Excellent guidance and quality material provided by the team.",
                profileImageResId = R.drawable.sample_profile_1
            )
        )
        val adapter = HallOfFameAdapter(hallOfFameList)
        setupHorizontalRecyclerView(recyclerView, adapter)
    }

    private fun setupStudentReviewRecyclerView(recyclerView: RecyclerView) {
        val studentReviewList = listOf(
            StudentReviewModel(
                testimonial = "The mock tests helped me identify my weak areas and improve my performance significantly. The detailed analysis after each test was incredibly helpful.",
                userName = "Nisha Chandarana",
                userDesignation = "Bin Sachivalay Clerk 2012",
                badgeText = "Dy Mamlatdar to be",
                avatarInitial = "N"
            ),
            StudentReviewModel(
                testimonial = "Best platform for competitive exam preparation. The content is top-notch and always updated with the latest syllabus.",
                userName = "Rajesh Kumar",
                userDesignation = "Talati Cum Mantri 2018",
                badgeText = "Section Officer to be",
                avatarInitial = "R"
            ),
            StudentReviewModel(
                testimonial = "I am grateful for the constant support from the mentors. They cleared all my doubts and motivated me throughout my journey.",
                userName = "Sunita Singh",
                userDesignation = "High Court Assistant 2020",
                badgeText = "Civil Judge to be",
                avatarInitial = "S"
            )
        )
        val adapter = StudentReviewAdapter(studentReviewList)
        setupHorizontalRecyclerView(recyclerView, adapter)
    }

    private fun setupHorizontalRecyclerView(
        recyclerView: RecyclerView,
        adapter: RecyclerView.Adapter<*>
    ) {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
    }
}