package com.example.niyuktikotlin.referals

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R

class ReferRewardsActivity : AppCompatActivity() {

    private lateinit var rvRecentReferrals: RecyclerView
    private lateinit var adapter: RecentReferalsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refer_rewards)

        rvRecentReferrals = findViewById(R.id.recent_refferals_rv)
        adapter = RecentReferalsAdapter()

        rvRecentReferrals.apply {
            layoutManager = LinearLayoutManager(this@ReferRewardsActivity)
            adapter = this@ReferRewardsActivity.adapter
        }
    }
}