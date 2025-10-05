package com.example.niyuktikotlin.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R
import com.example.niyuktikotlin.models.StudyMaterial

class StudyMaterialAdapter(
    private val itemList: List<StudyMaterial>
) : RecyclerView.Adapter<StudyMaterialAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_component_subject_resource, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var material = itemList[position]

        holder.image.setImageResource(material.image)
        holder.desc.text = material.desc
        holder.btn.text = material.btnTxt
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image : ImageView
        var desc : TextView
        var btn : TextView

        init {
            image = itemView.findViewById(R.id.study_material_image)
            desc = itemView.findViewById(R.id.study_material_desc)
            btn = itemView.findViewById(R.id.study_material_btn_text)
        }
    }
}