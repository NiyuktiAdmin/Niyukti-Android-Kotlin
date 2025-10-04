package com.example.niyuktikotlin.mock_test_list

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R

class MockTestListTagsAdapter : RecyclerView.Adapter<MockTestListTagsAdapter.ViewHolder>() {

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.filter_tag_component, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == 0) {
            holder.textView.setBackgroundColor(Color.BLACK)
            holder.textView.setTextColor(Color.WHITE)
        }

        holder.textView.text = "Trending"
    }

    override fun getItemCount(): Int { return 8 }

    class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.filter_tag_component_tv)
    }
}