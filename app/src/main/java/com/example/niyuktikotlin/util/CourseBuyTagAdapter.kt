package com.example.niyuktikotlin.util

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R

class CourseBuyTagAdapter (
    private val tags: List<String>,
    private val onTagSelectedListener: OnTagSelectedListener?
) : RecyclerView.Adapter<CourseBuyTagAdapter.ViewHolder>() {

    interface OnTagSelectedListener {
        fun onTagSelected(tagName: String)
    }

    private var selectedPosition: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.filter_tag_component, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.textView.text = tags[position]
        var context = holder.itemView.context

        if (position == selectedPosition) {
            holder.textView.setBackgroundColor(
                context.getColor(R.color.text_black)
            )
            holder.textView.setTextColor(
                context.getColor(R.color.text_white)
            )
        } else {
            holder.textView.setBackgroundColor(
                context.getColor(R.color.dialog_background)
            )
            holder.textView.setTextColor(
                context.getColor(R.color.text_black)
            )
        }

        holder.textView.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)

            onTagSelectedListener?.onTagSelected(tags[position])
        }
    }

    fun getSelectedTag(): String? {
        return tags.getOrNull(selectedPosition)
    }

    override fun getItemCount(): Int = tags.size

    class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.filter_tag_component_tv)
    }
}