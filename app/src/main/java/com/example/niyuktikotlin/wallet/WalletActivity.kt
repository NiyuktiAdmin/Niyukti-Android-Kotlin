package com.example.niyuktikotlin.wallet

import BaseActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R

class WalletActivity : BaseActivity() {
    private lateinit var transactionRv: RecyclerView
    private lateinit var adapter: WalletTransactionAdapter
    lateinit var backBtn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)

        transactionRv = findViewById(R.id.wallet_recentTransaction_rv)
        backBtn = findViewById(R.id.wallet_activity_back_btn)

        backBtn.setOnClickListener {
            finish()
        }

        transactionRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = WalletTransactionAdapter()
        transactionRv.adapter = adapter
    }
}