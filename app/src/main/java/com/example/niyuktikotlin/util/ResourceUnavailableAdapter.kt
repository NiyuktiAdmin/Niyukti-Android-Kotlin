package com.example.niyuktikotlin.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R

class ResourceUnavailableAdapter(
    private val pageText: String = "Resource not available",
    private val btnText: String? = null,
    private val onButtonClick: (() -> Unit)? = null
) : RecyclerView.Adapter<ResourceUnavailableAdapter.ViewHolder>() {

    override fun getItemCount() = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_res_unavailable, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.text.text = pageText

        if (btnText.isNullOrEmpty()) {
            holder.btn.visibility = View.GONE
        } else {
            holder.btn.visibility = View.VISIBLE
            holder.btn.text = btnText
            holder.btn.setOnClickListener {
                onButtonClick?.invoke()
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: TextView = itemView.findViewById(R.id.card_res_unavail_text)
        val btn: TextView = itemView.findViewById(R.id.card_res_unavail_button)
    }
}