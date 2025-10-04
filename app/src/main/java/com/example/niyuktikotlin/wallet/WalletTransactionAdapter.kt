package com.example.niyuktikotlin.wallet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R

class WalletTransactionAdapter : RecyclerView.Adapter<WalletTransactionAdapter.ViewHolder>() {

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_refferal_bonus, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        // Implementation for binding data goes here
    }

    override fun getItemCount(): Int {
        return 8
    }

    class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Add views here if needed, e.g.:
        // val someTextView: TextView = itemView.findViewById(R.id.some_text_view)
    }
}