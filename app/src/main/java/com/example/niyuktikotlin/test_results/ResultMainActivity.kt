package com.example.niyuktikotlin.test_results

import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R
import com.example.niyuktikotlin.home_page.HomeActivity

class ResultMainActivity : AppCompatActivity() {
    private lateinit var leaderBoardRv: RecyclerView
    private lateinit var scoreCircleBackground: FrameLayout
    private lateinit var homeBtn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_main)

        initialise()
        setCircleProgress()
        setupLeaderboardRecyclerView()

        homeBtn.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setCircleProgress() {
        val progressDrawable: Drawable? = scoreCircleBackground.background

        if (progressDrawable is LayerDrawable) {
            // Assuming the arc progress is the second layer (index 1)
            val arcDrawable: Drawable? = progressDrawable.getDrawable(1)

            if (arcDrawable != null) {
                // Calculate progress level. Max level for a Drawable is 10000.
                // Example: 72% progress
                val progress = (0.72 * 10000).toInt()
                arcDrawable.level = progress
            }
        }
    }

    private fun initialise() {
        scoreCircleBackground = findViewById(R.id.score_circle_frame_layout)
        leaderBoardRv = findViewById(R.id.result_main_leaderboard_rv)
        homeBtn = findViewById(R.id.result_main_home_btn)
    }

    private fun setupLeaderboardRecyclerView() {
        leaderBoardRv.layoutManager = LinearLayoutManager(this)
        val adapter = LeaderBoardAdapter()
        leaderBoardRv.adapter = adapter
    }
}