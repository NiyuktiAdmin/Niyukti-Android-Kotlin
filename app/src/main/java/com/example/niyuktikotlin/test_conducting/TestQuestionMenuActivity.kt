package com.example.niyuktikotlin.test_conducting
import BaseActivity
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.niyuktikotlin.R
import java.io.Serializable
import java.util.ArrayList
import java.util.LinkedHashMap
import java.util.Locale
import java.util.Map
import java.util.concurrent.TimeUnit

class TestQuestionMenuActivity : BaseActivity(), SubjectAdapter.OnSubjectSelectedListener {

    private lateinit var totalTimeRemainingTextView: TextView
    private lateinit var backButton: ImageView
    private lateinit var subjectsRvMenu: RecyclerView
    private lateinit var questionGridContainer: LinearLayout

    private var totalCountDownTimer: CountDownTimer? = null
    private var totalTimeLeftMillis: Long = 0

    private var allQuestionList: List<QuestionModel> = emptyList()
    private lateinit var subjectWiseQuestions: MutableMap<String, MutableList<QuestionModel>>
    private lateinit var subjectAdapter: SubjectAdapter

    private lateinit var answeredCountTv: TextView
    private lateinit var notAnsweredCountTv: TextView
    private lateinit var notVisitedCountTv: TextView
    private lateinit var markReviewCountTv: TextView
    private lateinit var markReviewAnsweredCountTv: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_question_menu)

        initiateViews()
        retrieveDataFromIntent()
        setupTotalTimeCounter()
        setupSubjectsRecyclerView()
        populateQuestionGrids()
        updateStatusIndicators()
        setupBackButton()
    }

    private fun initiateViews() {
        totalTimeRemainingTextView = findViewById(R.id.total_time_remaining)
        backButton = findViewById(R.id.back_button)
        subjectsRvMenu = findViewById(R.id.subjects_rv_menu)
        questionGridContainer = findViewById(R.id.question_grid_container)

        answeredCountTv = findViewById(R.id.answered_count)
        notAnsweredCountTv = findViewById(R.id.not_answered_count)
        notVisitedCountTv = findViewById(R.id.not_visited_count)
        markReviewCountTv = findViewById(R.id.mark_review_count)
        markReviewAnsweredCountTv = findViewById(R.id.mark_review_answered_count)
    }

    private fun retrieveDataFromIntent() {
        val intent = intent
        if (intent != null) {
            @Suppress("UNCHECKED_CAST")
            allQuestionList = intent.getSerializableExtra("allQuestionList") as? List<QuestionModel> ?: emptyList()
            totalTimeLeftMillis = intent.getLongExtra("totalTimeLeftMillis", 0)

            populateSubjectWiseQuestions()
        }
    }

    private fun populateSubjectWiseQuestions() {
        subjectWiseQuestions = LinkedHashMap()
        for (question in allQuestionList) {
            if (!subjectWiseQuestions.containsKey(question.subject)) {
                subjectWiseQuestions[question.subject] = mutableListOf()
            }
            subjectWiseQuestions[question.subject]!!.add(question)
        }
    }

    private fun setupTotalTimeCounter() {
        if (totalTimeLeftMillis > 0) {
            totalCountDownTimer = object : CountDownTimer(totalTimeLeftMillis, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    totalTimeLeftMillis = millisUntilFinished
                    totalTimeRemainingTextView.text = formatTime(millisUntilFinished)
                }

                override fun onFinish() {
                    totalTimeRemainingTextView.text = "00:00:00"
                }
            }.start()
        } else {
            totalTimeRemainingTextView.text = "00:00:00"
        }
    }

    private fun formatTime(millis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(hours)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun setupSubjectsRecyclerView() {
        val subjectNames = ArrayList(subjectWiseQuestions.keys)
        subjectAdapter = SubjectAdapter(subjectNames, this)
        subjectsRvMenu.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        subjectsRvMenu.adapter = subjectAdapter
    }

    private fun populateQuestionGrids() {
        questionGridContainer.removeAllViews()

        var globalQuestionNumber = 0

        for ((subjectName, questions) in subjectWiseQuestions) {
            // Add Subject Header (LinearLayout)
            val subjectHeaderLayout = LinearLayout(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                setPadding(0, dpToPx(16), 0, dpToPx(8))
            }

            val subjectTitle = TextView(this).apply {
                val titleParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f)
                layoutParams = titleParams
                text = subjectName
                textSize = 16f
                setTypeface(null, Typeface.BOLD)
                setTextColor(Color.BLACK)
            }
            subjectHeaderLayout.addView(subjectTitle)

            val questionRange = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)
                text = String.format(Locale.getDefault(), "(%d-%d)", globalQuestionNumber + 1, globalQuestionNumber + questions.size)
                textSize = 14f
                setTextColor(Color.DKGRAY)
            }
            subjectHeaderLayout.addView(questionRange)

            questionGridContainer.addView(subjectHeaderLayout)

            // Add Question Grid (GridLayout)
            val gridLayout = GridLayout(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)
                columnCount = 5
                alignmentMode = GridLayout.ALIGN_BOUNDS
                isColumnOrderPreserved = false
                useDefaultMargins = true
            }

            // Calculate cell width dynamically
            val screenWidthPx = resources.displayMetrics.widthPixels
            val paddingPx = dpToPx(16)
            val marginPx = dpToPx(4)
            val totalHorizontalSpaceForCells = screenWidthPx - (2 * paddingPx) - (4 * marginPx)
            val cellWidth = totalHorizontalSpaceForCells / 6

            for (i in questions.indices) {
                val currentGlobalIndex = globalQuestionNumber + i
                val question = questions[i]

                val questionNumberTextView = TextView(this).apply {
                    val params = GridLayout.LayoutParams()
                    params.width = cellWidth
                    params.height = cellWidth
                    params.setMargins(marginPx, marginPx, marginPx, marginPx)
                    layoutParams = params
                    text = (currentGlobalIndex + 1).toString()
                    textSize = 16f
                    gravity = Gravity.CENTER
                    setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8))

                    // Set background based on question state
                    when {
                        question.markedForReview && question.selectedOptionIndex != -1 -> {
                            setBackgroundResource(R.drawable.rounded_dark_green_with_dot_bg) // Marked for Review and Answered
                            setTextColor(Color.WHITE)
                        }
                        question.markedForReview -> {
                            setBackgroundResource(R.drawable.rounded_purple_bg) // Marked for Review
                            setTextColor(Color.WHITE)
                        }
                        question.selectedOptionIndex != -1 -> {
                            setBackgroundResource(R.drawable.rounded_green_bg) // Answered
                            setTextColor(Color.WHITE)
                        }
                        question.selectedOptionIndex == -1 && question.timeSpent > 0 -> {
                            setBackgroundResource(R.drawable.rounded_red_bg) // Not Answered (Visited)
                            setTextColor(Color.WHITE)
                        }
                        else -> {
                            setBackgroundResource(R.drawable.rounded_grey_bg) // Not Visited
                            setTextColor(Color.BLACK)
                        }
                    }

                    // Set click listener to go back to TestMainActivity
                    setOnClickListener {
                        val resultIntent = Intent()
                        resultIntent.putExtra("selectedQuestionIndex", currentGlobalIndex)
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    }
                }
                gridLayout.addView(questionNumberTextView)
            }
            questionGridContainer.addView(gridLayout)
            globalQuestionNumber += questions.size
        }
    }

    private fun updateStatusIndicators() {
        var answeredCount = 0
        var notAnsweredCount = 0
        var notVisitedCount = 0
        var markedForReviewCount = 0
        var markedForReviewAndAnsweredCount = 0

        for (question in allQuestionList) {
            if (question.selectedOptionIndex != -1) {
                answeredCount++
                if (question.markedForReview) {
                    markedForReviewAndAnsweredCount++
                }
            } else {
                if (question.markedForReview) {
                    markedForReviewCount++
                } else if (question.timeSpent > 0) {
                    notAnsweredCount++
                } else {
                    notVisitedCount++
                }
            }
        }

        answeredCountTv.text = answeredCount.toString()
        notAnsweredCountTv.text = notAnsweredCount.toString()
        notVisitedCountTv.text = notVisitedCount.toString()
        markReviewCountTv.text = markedForReviewCount.toString()
        markReviewAnsweredCountTv.text = markedForReviewAndAnsweredCount.toString()
    }


    private fun setupBackButton() {
        backButton.setOnClickListener {
            finish()
        }
    }

    // Helper method to convert dp to pixels
    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    override fun onDestroy() {
        super.onDestroy()
        totalCountDownTimer?.cancel()
    }

    override fun onSubjectSelected(subjectName: String) {
        // Implementation is intentionally empty as per the original Java code's requirements
    }
}