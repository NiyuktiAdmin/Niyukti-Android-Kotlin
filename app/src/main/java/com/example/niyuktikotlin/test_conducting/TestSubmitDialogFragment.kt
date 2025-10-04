package com.example.niyuktikotlin.test_conducting

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import com.example.niyuktikotlin.R
import java.util.Locale
import java.util.concurrent.TimeUnit

class TestSubmitDialogFragment : DialogFragment() {

    private var timeLeftMillis: Long = 0
    private var totalQuestions: Int = 0
    private var answeredQuestions: Int = 0
    private var notAnsweredQuestions: Int = 0
    private var markedForReviewQuestions: Int = 0

    // Interface to communicate with the hosting activity
    interface OnSubmitClickListener {
        fun onSubmitConfirmed()
    }

    private var listener: OnSubmitClickListener? = null

    fun setOnSubmitClickListener(listener: OnSubmitClickListener) {
        this.listener = listener
    }

    companion object {
        private const val ARG_TIME_LEFT = "timeLeftMillis"
        private const val ARG_TOTAL_Q = "totalQuestions"
        private const val ARG_ANSWERED_Q = "answeredQuestions"
        private const val ARG_NOT_ANSWERED_Q = "notAnsweredQuestions"
        private const val ARG_MARKED_REVIEW_Q = "markedForReviewQuestions"

        fun newInstance(timeLeftMillis: Long, totalQuestions: Int,
                        answeredQuestions: Int, notAnsweredQuestions: Int,
                        markedForReviewQuestions: Int): TestSubmitDialogFragment {
            val fragment = TestSubmitDialogFragment()
            val args = Bundle().apply {
                putLong(ARG_TIME_LEFT, timeLeftMillis)
                putInt(ARG_TOTAL_Q, totalQuestions)
                putInt(ARG_ANSWERED_Q, answeredQuestions)
                putInt(ARG_NOT_ANSWERED_Q, notAnsweredQuestions)
                putInt(ARG_MARKED_REVIEW_Q, markedForReviewQuestions)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.FullScreenDialogStyle)

        arguments?.let {
            timeLeftMillis = it.getLong(ARG_TIME_LEFT)
            totalQuestions = it.getInt(ARG_TOTAL_Q)
            answeredQuestions = it.getInt(ARG_ANSWERED_Q)
            notAnsweredQuestions = it.getInt(ARG_NOT_ANSWERED_Q)
            markedForReviewQuestions = it.getInt(ARG_MARKED_REVIEW_Q)
        }
    }

    override fun onCreateView(@NonNull inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.dialogue_test_submit, container, false)

        val tvTimeRemaining: TextView = view.findViewById(R.id.dialog_test_submit_time_remaining)
        val tvTotalQuestions: TextView = view.findViewById(R.id.dialog_test_submit_total_questions)
        val tvAnsweredQuestions: TextView = view.findViewById(R.id.dialog_test_submit_answered_questions)
        val tvNotAnsweredQuestions: TextView = view.findViewById(R.id.dialog_test_submit_not_answered_questions)
        val tvMarkedForReviewQuestions: TextView = view.findViewById(R.id.dialog_test_submit_marked_for_review_questions)
        val confirmSubmitButton: LinearLayout? = view.findViewById(R.id.dialog_test_submit_confirm_btn)

        tvTimeRemaining.text = formatTime(timeLeftMillis)
        tvTotalQuestions.text = totalQuestions.toString()
        tvAnsweredQuestions.text = answeredQuestions.toString()
        tvNotAnsweredQuestions.text = notAnsweredQuestions.toString()
        tvMarkedForReviewQuestions.text = markedForReviewQuestions.toString()

        confirmSubmitButton?.setOnClickListener {
            listener?.onSubmitConfirmed()
            dismiss()
        }

        dialog?.window?.apply {
            requestFeature(Window.FEATURE_NO_TITLE)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes = attributes.apply {
                gravity = Gravity.BOTTOM
                width = WindowManager.LayoutParams.MATCH_PARENT
                height = WindowManager.LayoutParams.WRAP_CONTENT
            }
        }

        return view
    }

    private fun formatTime(millis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(hours)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
    }
}