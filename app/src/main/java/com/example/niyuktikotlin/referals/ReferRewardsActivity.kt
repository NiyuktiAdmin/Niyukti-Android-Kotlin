package com.example.niyuktikotlin.referals

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R

class ReferRewardsActivity : AppCompatActivity() {

    private lateinit var rvRecentReferrals: RecyclerView
    private lateinit var adapter: RecentReferalsAdapter
    private lateinit var backBtn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refer_rewards)

        backBtn = findViewById(R.id.refer_earn_back_btn)
        rvRecentReferrals = findViewById(R.id.recent_refferals_rv)
        adapter = RecentReferalsAdapter()

        backBtn.setOnClickListener {
            finish()
        }
        rvRecentReferrals.apply {
            layoutManager = LinearLayoutManager(this@ReferRewardsActivity)
            adapter = this@ReferRewardsActivity.adapter
        }
    }
}