package com.example.niyuktikotlin.util

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R
import com.example.niyuktikotlin.mock_test_list.MockTestSubListActivity
import com.example.niyuktikotlin.models.CourseFolder

class CourseFileAdapter (
    private val courseList: List<CourseFolder>,
    private val destinationActivity: Class<*>
) : RecyclerView.Adapter<CourseFileAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseFileAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_item_folder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val courseItem = courseList[position]

        holder.name.text = courseItem.title
        holder.desc.text = courseItem.desc

        holder.body.setOnClickListener { v ->
            val intent = Intent(v.context, destinationActivity)
            intent.putExtra("courseId", courseItem.id)
            v.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = courseList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name : TextView
        var body : LinearLayout
        var desc : TextView
        init {
            name = itemView.findViewById(R.id.card_item_folder_title)
            body = itemView.findViewById(R.id.card_item_folder_body)
            desc = itemView.findViewById(R.id.card_item_folder_description)
        }
    }
}