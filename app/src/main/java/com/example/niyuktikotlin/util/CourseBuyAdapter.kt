package com.example.niyuktikotlin.util

import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R
import com.example.niyuktikotlin.mock_test_list.MockTestSubListActivity
import com.example.niyuktikotlin.models.CourseModel

class CourseBuyAdapter(
    private val courseList: List<CourseModel>
) : RecyclerView.Adapter<CourseBuyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_course_component, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val course = courseList[position]

        holder.cardImage.setImageResource(course.imageResId)
        holder.cardTitle.text = course.title
        holder.cardNoAmt.text = "₹ ${course.actualPrice}"
        holder.cardAmt.text = "₹ ${course.discountedPrice}"
        holder.cardDiscount.text = "${course.discountPercent}% OFF"

        holder.mainBtn.setOnClickListener { v ->
            val intent = Intent(v.context, MockTestSubListActivity::class.java)
            v.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = courseList.size // Use list size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // ... (init block remains the same)
        var cardImage: ImageView
        var cardTitle: TextView
        var cardAmt: TextView
        var cardNoAmt: TextView
        var cardDiscount: TextView
        var mainBtn: CardView
        var shareBtn: CardView

        init {
            cardImage = itemView.findViewById(R.id.course_component_image)
            cardTitle = itemView.findViewById(R.id.course_component_title)
            cardAmt = itemView.findViewById(R.id.course_component_cost)
            cardNoAmt = itemView.findViewById(R.id.course_component_nocost)
            cardDiscount = itemView.findViewById(R.id.course_component_discount_tv)
            mainBtn = itemView.findViewById(R.id.course_component_main_btn)
            shareBtn = itemView.findViewById(R.id.course_component_share_btn)
            cardNoAmt.paintFlags = cardNoAmt.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }
}