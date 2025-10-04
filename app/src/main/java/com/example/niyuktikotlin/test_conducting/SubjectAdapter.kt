package com.example.niyuktikotlin.test_conducting

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R
import kotlin.collections.List

class SubjectAdapter(
    private val subjectList: List<String>,
    private val onSubjectSelectedListener: OnSubjectSelectedListener?
) : RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder>() {

    private var selectedPosition: Int = 0 // Initially select the first subject

    interface OnSubjectSelectedListener {
        fun onSubjectSelected(subjectName: String)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.filter_tag_component, parent, false)
        return SubjectViewHolder(view)
    }

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        val subject = subjectList[position]
        holder.subjectTextView.text = subject

        if (position == selectedPosition) {
            holder.subjectTextView.setBackgroundColor(Color.parseColor("#2D6EFF"))
            holder.subjectTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, android.R.color.white))
        } else {
            holder.subjectTextView.setBackgroundColor(Color.parseColor("#FFFFFF"))
            holder.subjectTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, android.R.color.black))
        }

        holder.itemView.setOnClickListener {
            val previousSelectedPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(previousSelectedPosition) // Deselect previous
            notifyItemChanged(selectedPosition) // Select new

            onSubjectSelectedListener?.onSubjectSelected(subject)
        }
    }

    override fun getItemCount(): Int {
        return subjectList.size
    }

    class SubjectViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subjectTextView: TextView = itemView.findViewById(R.id.filter_tag_component_tv)
    }

    fun getSelectedSubject(): String? {
        if (subjectList.isNotEmpty() && selectedPosition != RecyclerView.NO_POSITION) {
            return subjectList[selectedPosition]
        }
        return null
    }

    // New method to update selected position programmatically
    fun setSelectedPosition(position: Int) {
        if (this.selectedPosition != position) {
            val oldPosition = this.selectedPosition
            this.selectedPosition = position
            notifyItemChanged(oldPosition)
            notifyItemChanged(this.selectedPosition)
        }
    }
}