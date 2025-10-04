package com.example.niyuktikotlin.test_conducting

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R

class QuestionAdapter(
    private val questionList: List<QuestionModel>,
    private val onOptionSelectedListener: OnOptionSelectedListener?
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    interface OnOptionSelectedListener {
        fun onOptionSelected(questionPosition: Int, selectedOptionIndex: Int)
        fun onOptionClick(questionPosition: Int, selectedOptionIndex: Int)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_component_test_question, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questionList[position]
        val context = holder.itemView.context

        holder.tvQuestion.text = question.questionText
        holder.optionA.text = question.options[0]
        holder.optionB.text = question.options[1]
        holder.optionC.text = question.options[2]
        holder.optionD.text = question.options[3]

        // Accessing the first child (LinearLayout) of the CardView's content
        val llOptionA = holder.cardOptionA.getChildAt(0) as LinearLayout
        val llOptionB = holder.cardOptionB.getChildAt(0) as LinearLayout
        val llOptionC = holder.cardOptionC.getChildAt(0) as LinearLayout
        val llOptionD = holder.cardOptionD.getChildAt(0) as LinearLayout

        // Reset background resources to default border
        llOptionA.setBackgroundResource(R.drawable.bgrd_border)
        llOptionB.setBackgroundResource(R.drawable.bgrd_border)
        llOptionC.setBackgroundResource(R.drawable.bgrd_border)
        llOptionD.setBackgroundResource(R.drawable.bgrd_border)

        // Highlight selected option using when expression (Kotlin's switch)
        when (question.selectedOptionIndex) {
            0 -> llOptionA.setBackgroundColor(Color.parseColor("#24B655"))
            1 -> llOptionB.setBackgroundColor(Color.parseColor("#24B655"))
            2 -> llOptionC.setBackgroundColor(Color.parseColor("#24B655"))
            3 -> llOptionD.setBackgroundColor(Color.parseColor("#24B655"))
        }

        // Helper function for click logic
        fun setOptionClickListener(cardView: CardView, optionIndex: Int) {
            cardView.setOnClickListener {
                question.selectedOptionIndex = optionIndex
                onOptionSelectedListener?.onOptionClick(position, optionIndex)
                notifyItemChanged(position)
                onOptionSelectedListener?.onOptionSelected(position, optionIndex)
            }
        }

        // Set click listeners
        setOptionClickListener(holder.cardOptionA, 0)
        setOptionClickListener(holder.cardOptionB, 1)
        setOptionClickListener(holder.cardOptionC, 2)
        setOptionClickListener(holder.cardOptionD, 3)
    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    class QuestionViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvQuestion: TextView = itemView.findViewById(R.id.tvQuestion)
        val optionA: TextView = itemView.findViewById(R.id.optionA)
        val optionB: TextView = itemView.findViewById(R.id.optionB)
        val optionC: TextView = itemView.findViewById(R.id.optionC)
        val optionD: TextView = itemView.findViewById(R.id.optionD)
        val cardOptionA: CardView = itemView.findViewById(R.id.cardOptionA)
        val cardOptionB: CardView = itemView.findViewById(R.id.cardOptionB)
        val cardOptionC: CardView = itemView.findViewById(R.id.cardOptionC)
        val cardOptionD: CardView = itemView.findViewById(R.id.cardOptionD)
    }
}