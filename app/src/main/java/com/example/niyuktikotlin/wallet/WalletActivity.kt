package com.example.niyuktikotlin.wallet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R

class WalletActivity : AppCompatActivity() {
    private lateinit var transactionRv: RecyclerView
    private lateinit var adapter: WalletTransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)

        transactionRv = findViewById(R.id.wallet_recentTransaction_rv)

        transactionRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = WalletTransactionAdapter()
        transactionRv.adapter = adapter
    }
}