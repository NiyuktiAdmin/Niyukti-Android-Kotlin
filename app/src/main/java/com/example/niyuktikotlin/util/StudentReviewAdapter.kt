package com.example.niyuktikotlin.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R
import com.example.niyuktikotlin.models.StudentReviewModel

class StudentReviewAdapter(
    private val reviewList: List<StudentReviewModel>
) : RecyclerView.Adapter<StudentReviewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_student_review, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = reviewList[position]
        holder.testimonialText.text = review.testimonial
        holder.userName.text = review.userName
        holder.userDesignation.text = review.userDesignation
        holder.badge.text = review.badgeText
        holder.avatar.text = review.avatarInitial
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val testimonialText: TextView = itemView.findViewById(R.id.testimonial_text)
        val userName: TextView = itemView.findViewById(R.id.user_name)
        val userDesignation: TextView = itemView.findViewById(R.id.user_designation)
        val badge: TextView = itemView.findViewById(R.id.dy_mamlatdar_badge)
        val avatar: TextView = itemView.findViewById(R.id.user_avatar)
    }
}