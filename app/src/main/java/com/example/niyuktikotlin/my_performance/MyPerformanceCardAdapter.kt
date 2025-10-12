package com.example.niyuktikotlin.my_performance

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R
import com.example.niyuktikotlin.models.CourseFolder

class MyPerformanceCardAdapter (
    private val itemList: List<CourseFolder>
    ) : RecyclerView.Adapter<MyPerformanceCardAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.card_my_performance, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.title.text = itemList[position].title
        }

        override fun getItemCount(): Int {
            return itemList.size
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val title: TextView = itemView.findViewById(R.id.card_mpa_title)
        }
    }