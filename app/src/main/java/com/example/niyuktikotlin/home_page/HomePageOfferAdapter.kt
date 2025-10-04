package com.example.niyuktikotlin.home_page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R

class HomePageOfferAdapter : RecyclerView.Adapter<HomePageOfferAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_home_course_adv, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cardName.text = "Click Here to Buy Course"
        holder.cardImage.setImageResource(R.drawable.sample_home_offer)
    }

    override fun getItemCount(): Int {
        return 6
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardName: TextView
        var cardImage: ImageView

        init {
            cardImage = itemView.findViewById(R.id.home_course_card_img)
            cardName = itemView.findViewById(R.id.home_course_card_name)
        }
    }
}