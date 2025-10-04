package com.example.niyuktikotlin.login

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R

class LoginCardAdapter : RecyclerView.Adapter<LoginCardAdapter.ViewHolder>() {

    private val cardData = listOf(
        Triple("Learn Anywhere", "Access courses on the go", R.drawable.ic_books),
        Triple("Track Progress", "Monitor your learning journey", R.drawable.ic_target),
        Triple("Achieve Goals", "Unlock your potential", R.drawable.ic_trophy)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.login_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = cardData.getOrNull(position) ?: cardData.last()

        holder.cardTitle.text = data.first
        holder.cardDescription.text = data.second
        holder.cardIcon.setImageResource(data.third)
    }

    override fun getItemCount(): Int = cardData.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardTitle: TextView = itemView.findViewById(R.id.login_card_title)
        val cardDescription: TextView = itemView.findViewById(R.id.login_card_desc)
        val cardIcon: ImageView = itemView.findViewById(R.id.login_card_emojie)
    }
}