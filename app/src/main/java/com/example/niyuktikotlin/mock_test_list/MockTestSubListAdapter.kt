package com.example.niyuktikotlin.mock_test_list

//import android.content.Intent
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.LinearLayout
//import android.widget.TextView
//import androidx.annotation.NonNull
//import androidx.recyclerview.widget.RecyclerView
//import com.example.niyuktikotlin.R
//import com.example.niyuktikotlin.course_resources.CourseResourcesActivity
//
//class MockTestSubListAdapter : RecyclerView.Adapter<MockTestSubListAdapter.ViewHolder>() {
//
//    @NonNull
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.card_item_folder, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.title.text = "Constable Mock Test ${position + 1}"
//
//        holder.body.setOnClickListener { v ->
//            val intent = Intent(v.context, CourseResourcesActivity::class.java)
//            v.context.startActivity(intent)
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return 6
//    }
//
//    class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val title: TextView = itemView.findViewById(R.id.card_item_folder_title)
//        val description: TextView = itemView.findViewById(R.id.card_item_folder_description)
//        val body: LinearLayout = itemView.findViewById(R.id.card_item_folder_body)
//    }
//}