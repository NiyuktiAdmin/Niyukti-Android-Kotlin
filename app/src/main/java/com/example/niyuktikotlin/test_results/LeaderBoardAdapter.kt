package com.example.niyuktikotlin.test_results

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R
import androidx.annotation.NonNull

class LeaderBoardAdapter : RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder>() {

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_test_result_leaderboard, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        if (position != 0) {
            holder.you.visibility = View.GONE
        } else {
            holder.you.visibility = View.VISIBLE
        }

        updateRankingImages(position, holder)
    }

    private fun updateRankingImages(position: Int, holder: ViewHolder) {
        holder.rankImg.removeAllViews()

        when (position) {
            0 -> {
                val imageView = ImageView(holder.itemView.context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT)
                    setImageResource(R.drawable.leaderboard_first_rank)
                    scaleType = ImageView.ScaleType.CENTER_INSIDE
                    setPadding(12, 12, 12, 12)
                }
                holder.rankImg.addView(imageView)
            }
            1 -> {
                val imageView = ImageView(holder.itemView.context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT)
                    setImageResource(R.drawable.leaderboard_second_rank)
                    scaleType = ImageView.ScaleType.CENTER_INSIDE
                    setPadding(12, 12, 12, 12)
                }
                holder.rankImg.addView(imageView)
            }
            2 -> {
                val imageView = ImageView(holder.itemView.context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT)
                    setImageResource(R.drawable.leaderboard_third_rank)
                    scaleType = ImageView.ScaleType.CENTER_INSIDE
                    setPadding(12, 12, 12, 12)
                }
                holder.rankImg.addView(imageView)
            }
            else -> {
                val rankText = TextView(holder.itemView.context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT)
                    val s = "#${position + 1}"
                    text = s
                    setTextColor(ContextCompat.getColor(holder.itemView.context, android.R.color.black))
                    setBackgroundColor(ContextCompat.getColor(holder.itemView.context, android.R.color.darker_gray))
                    textSize = 20f
                    gravity = Gravity.CENTER
                }
                holder.rankImg.addView(rankText)
            }
        }
    }

    override fun getItemCount(): Int {
        return 5
    }

    class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        val you: TextView = itemView.findViewById(R.id.card_leaderboard_you_text)
        val rankImg: CardView = itemView.findViewById(R.id.leaderboard_card_rank_img)
    }
}