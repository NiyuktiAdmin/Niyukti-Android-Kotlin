package com.example.niyuktikotlin.course_resources

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R
import com.example.niyuktikotlin.test_info.GeneralTestInstructionsActivity

class CoursesTestsAdapter : RecyclerView.Adapter<CoursesTestsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_listitem_attempt_test, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.attemptBtn.setOnClickListener { v ->
            val intent = Intent(v.context, GeneralTestInstructionsActivity::class.java)
            v.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return 1
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val attemptBtn: TextView = itemView.findViewById(R.id.listitem_attempt_test_btn)
    }
}