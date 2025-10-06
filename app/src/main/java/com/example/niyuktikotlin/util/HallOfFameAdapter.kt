package com.example.niyuktikotlin.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R
import com.example.niyuktikotlin.models.HallOfFameModel

class HallOfFameAdapter(
    private val fameList: List<HallOfFameModel>
) : RecyclerView.Adapter<HallOfFameAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_hall_of_fame, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val person = fameList[position]

        // This line correctly sets the image from the model's resource ID
        holder.profileImage.setImageResource(person.profileImageResId)

        holder.userName.text = person.userName
        holder.badge.text = person.achievement
        holder.testimonial.text = person.testimonial
    }

    override fun getItemCount(): Int {
        return fameList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // This finds the ImageView in card_hall_of_fame.xml
        val profileImage: ImageView = itemView.findViewById(R.id.profile_image)
        val userName: TextView = itemView.findViewById(R.id.user_name)
        val badge: TextView = itemView.findViewById(R.id.psi_badge)
        val testimonial: TextView = itemView.findViewById(R.id.testimonial_text)
    }
}