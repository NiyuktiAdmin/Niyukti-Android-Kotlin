package com.example.niyuktikotlin.referals

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R


class RecentReferalsAdapter : RecyclerView.Adapter<RecentReferalsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_recent_referral, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position % 3 == 0) {
            holder.status.text = "success"
            holder.status.setBackgroundColor(Color.GREEN)
        }
    }

    override fun getItemCount(): Int {
        return 28
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView? = null
        var date: TextView? = null
        var status: TextView
        var reward: TextView? = null

        init {
            status = itemView.findViewById<TextView>(R.id.recent_referral_status)
        }
    }
}
