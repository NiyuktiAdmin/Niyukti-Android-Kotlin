package com.example.niyuktikotlin.test_conducting

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.niyuktikotlin.R
import com.example.niyuktikotlin.test_results.ResultMainActivity
import java.io.Serializable
import java.util.ArrayList
import java.util.LinkedHashMap
import java.util.Locale
import java.util.concurrent.TimeUnit

class TestMainActivity : AppCompatActivity(), QuestionAdapter.OnOptionSelectedListener, SubjectAdapter.OnSubjectSelectedListener, TestSubmitDialogFragment.OnSubmitClickListener {
    private lateinit var totalTimeRemaining: TextView
    private lateinit var currQuesNo: TextView
    private lateinit var currPassedTime: TextView
    private lateinit var nextButtonText: TextView
    private lateinit var nextQuesBtn: LinearLayout
    private lateinit var prevQuesBtn: LinearLayout
    private lateinit var subjectsRv: RecyclerView
    private lateinit var questionsRv: RecyclerView
    private lateinit var infoButton: ImageView
    private lateinit var backButton: ImageView
    private lateinit var markReviewButton: ImageView
    private lateinit var questionMenuButton: ImageView

    private var totalCountDownTimer: CountDownTimer? = null
    private val totalTimeInMillis = TimeUnit.HOURS.toMillis(1) // 1 hour
    private var totalTimeLeftMillis: Long = 0 // To store remaining time

    private lateinit var subjectAdapter: SubjectAdapter
    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var allQuestionList: MutableList<QuestionModel> // All questions loaded
    private lateinit var currentSubjectQuestionList: MutableList<QuestionModel> // Questions for the currently selected subject
    private lateinit var subjectWiseQuestions: MutableMap<String, MutableList<QuestionModel>> // Map to store questions by subject

    private var currentQuestionIndexInSubject = 0 // Index within the current subject's questions
    private var currentSubject = ""

    private var currentQuestionTimeHandler: Handler? = null
    private var currentQuestionTimeRunnable: Runnable? = null

    private lateinit var questionMenuLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_main)

        initiateValues()
        setupTotalTimeCounter()
        loadAllQuestions() // Load all questions first

        // Initialize currentSubject with the first subject and populate currentSubjectQuestionList
        if (subjectWiseQuestions.isNotEmpty()) {
            currentSubject = subjectWiseQuestions.keys.first()
            currentSubjectQuestionList = subjectWiseQuestions[currentSubject]!! // Initialize this here
        }

        setupSubjectsRecyclerView()
        setupQuestionsRecyclerView() // Call this after currentSubjectQuestionList is populated
        setupNavigationButtons()
        setupInfoAndBackButtons()
        setupMarkReviewButton() // Setup mark review button
        setupQuestionMenuButton() // Setup question menu button
        startCurrentQuestionTimer()
        updateQuestionDisplay() // Call update display to show Q-1
        updateMarkReviewButtonUI() // Update review button state for initial question

        // Initialize ActivityResultLauncher
        questionMenuLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val selectedGlobalQuestionIndex = result.data!!.getIntExtra("selectedQuestionIndex", -1)
                if (selectedGlobalQuestionIndex != -1) {
                    // Find the subject and local index based on the global index
                    var cumulativeCount = 0
                    var targetSubject: String? = null
                    var targetLocalIndex = -1

                    val subjectNames = ArrayList(subjectWiseQuestions.keys)
                    for (subject in subjectNames) {
                        val questions = subjectWiseQuestions[subject]!!
                        if (selectedGlobalQuestionIndex >= cumulativeCount && selectedGlobalQuestionIndex < cumulativeCount + questions.size) {
                            targetSubject = subject
                            targetLocalIndex = selectedGlobalQuestionIndex - cumulativeCount
                            break
                        }
                        cumulativeCount += questions.size
                    }

                    if (targetSubject != null) {
                        // Update current subject and question index
                        currentSubject = targetSubject
                        currentSubjectQuestionList = subjectWiseQuestions[currentSubject]!!
                        currentQuestionIndexInSubject = targetLocalIndex

                        // Update subject RV selection
                        subjectAdapter.setSelectedPosition(subjectNames.indexOf(currentSubject))
                        subjectsRv.scrollToPosition(subjectNames.indexOf(currentSubject))

                        // Update questions RV
                        questionsRv.suppressLayout(false) // Unfreeze layout
                        questionAdapter = QuestionAdapter(currentSubjectQuestionList, this)
                        questionsRv.adapter = questionAdapter
                        questionsRv.scrollToPosition(currentQuestionIndexInSubject)
                        questionsRv.suppressLayout(true) // Freeze layout

                        updateQuestionDisplay()
                        updateNavigationButtonsVisibility()
                        updateMarkReviewButtonUI() // Update review button state for the new question
                        startCurrentQuestionTimer() // Restart timer for the new question
                    }
                }
            }
        }
    }

    private fun initiateValues() {
        totalTimeRemaining = findViewById(R.id.test_main_total_time_left)
        currQuesNo = findViewById(R.id.test_main_ques_no)
        currPassedTime = findViewById(R.id.test_main_curr_passed_time)
        nextButtonText = findViewById(R.id.tvNext) // Initialize nextButtonText

        nextQuesBtn = findViewById(R.id.test_main_next_q_btn)
        prevQuesBtn = findViewById(R.id.test_main_prev_q_btn)

        subjectsRv = findViewById(R.id.test_main_subject_rv)
        questionsRv = findViewById(R.id.test_main_questions_rv)

        infoButton = findViewById(R.id.test_main_info_btn)
        backButton = findViewById(R.id.test_main_back_btn)
        markReviewButton = findViewById(R.id.test_main_mark_review_btn) // Initialize mark review button
        questionMenuButton = findViewById(R.id.test_main_question_menu_btn) // Initialize question menu button
    }

    private fun setupTotalTimeCounter() {
        totalCountDownTimer = object : CountDownTimer(totalTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                totalTimeLeftMillis = millisUntilFinished // Update remaining time
                totalTimeRemaining.text = formatTime(millisUntilFinished)
            }

            override fun onFinish() {
                totalTimeRemaining.text = "00:00:00"
                Toast.makeText(this@TestMainActivity, "Test Finished!", Toast.LENGTH_SHORT).show()
                // Optionally show submit dialog here if test finishes by time
                showSubmitDialog()
            }
        }.start()
    }

    private fun formatTime(millis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(hours)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun loadAllQuestions() {
        allQuestionList = mutableListOf()
        subjectWiseQuestions = LinkedHashMap() // Use LinkedHashMap to preserve subject order

        // Mathematics Questions
        allQuestionList.add(QuestionModel("Q1: What is the square root of 64?",
            arrayOf("6", "7", "8", "9"), 2, "Mathematics"))
        allQuestionList.add(QuestionModel("Q2: What is 15 × 3?",
            arrayOf("30", "45", "60", "35"), 1, "Mathematics"))
        allQuestionList.add(QuestionModel("Q3: What is the value of π (pi) approximately?",
            arrayOf("2.12", "3.14", "3.41", "4.13"), 1, "Mathematics"))

// Science Questions
        allQuestionList.add(QuestionModel("Q4: What planet is known as the Red Planet?",
            arrayOf("Venus", "Mars", "Jupiter", "Mercury"), 1, "Science"))
        allQuestionList.add(QuestionModel("Q5: What is the chemical symbol for water?",
            arrayOf("H2O", "CO2", "O2", "NaCl"), 0, "Science"))
        allQuestionList.add(QuestionModel("Q6: What gas do plants absorb during photosynthesis?",
            arrayOf("Oxygen", "Carbon Dioxide", "Nitrogen", "Hydrogen"), 1, "Science"))

// History Questions
        allQuestionList.add(QuestionModel("Q7: Who was the first President of the United States?",
            arrayOf("Abraham Lincoln", "George Washington", "John Adams", "Thomas Jefferson"), 1, "History"))
        allQuestionList.add(QuestionModel("Q8: In which year did World War II end?",
            arrayOf("1940", "1942", "1945", "1948"), 2, "History"))
        allQuestionList.add(QuestionModel("Q9: Who discovered America?",
            arrayOf("Christopher Columbus", "Vasco da Gama", "Marco Polo", "James Cook"), 0, "History"))

// Geography Questions
        allQuestionList.add(QuestionModel("Q10: What is the capital of Japan?",
            arrayOf("Beijing", "Seoul", "Tokyo", "Bangkok"), 2, "Geography"))
        allQuestionList.add(QuestionModel("Q11: Which is the largest ocean on Earth?",
            arrayOf("Atlantic", "Indian", "Arctic", "Pacific"), 3, "Geography"))
        allQuestionList.add(QuestionModel("Q12: Which country has the longest coastline?",
            arrayOf("Australia", "Russia", "Canada", "USA"), 2, "Geography"))

        // Populate subjectWiseQuestions map
        for (question in allQuestionList) {
            if (!subjectWiseQuestions.containsKey(question.subject)) {
                subjectWiseQuestions[question.subject] = mutableListOf()
            }
            subjectWiseQuestions[question.subject]!!.add(question)
        }
    }

    private fun setupSubjectsRecyclerView() {
        val subjectNames = ArrayList(subjectWiseQuestions.keys)
        subjectAdapter = SubjectAdapter(subjectNames, this)
        subjectsRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        subjectsRv.adapter = subjectAdapter
    }

    // This method is now responsible for handling full subject changes and re-rendering the questions RV
    private fun updateQuestionsForSelectedSubject(subject: String) {
        stopCurrentQuestionTimer()
        currentSubject = subject
        currentSubjectQuestionList = subjectWiseQuestions[subject]!!
        currentQuestionIndexInSubject = 0 // Always reset to the first question of the new subject

        // Always re-create and set the adapter to force a full redraw when subject changes
        questionsRv.suppressLayout(false) // Unfreeze before updating adapter
        questionAdapter = QuestionAdapter(currentSubjectQuestionList, this)
        questionsRv.adapter = questionAdapter
        questionsRv.scrollToPosition(currentQuestionIndexInSubject)
        questionsRv.suppressLayout(true) // Re-suppress after programmatic scroll completes

        updateNavigationButtonsVisibility()
        updateQuestionDisplay()
        updateMarkReviewButtonUI() // Update review button state for the new question
        startCurrentQuestionTimer()
    }

    private fun setupQuestionsRecyclerView() {
        questionAdapter = QuestionAdapter(currentSubjectQuestionList, this)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        questionsRv.layoutManager = layoutManager
        questionsRv.adapter = questionAdapter

        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(questionsRv)
        questionsRv.suppressLayout(true) // Prevent manual swiping initially

        updateNavigationButtonsVisibility()
        updateQuestionDisplay()
    }

    private fun setupNavigationButtons() {
        nextQuesBtn.setOnClickListener {
            questionsRv.suppressLayout(false) // Unsuppress layout for any updates/scrolls
            val subjectNames = ArrayList(subjectWiseQuestions.keys)
            val isLastQuestionOfLastSubject = (currentQuestionIndexInSubject == currentSubjectQuestionList.size - 1 &&
                    subjectNames.indexOf(currentSubject) == subjectWiseQuestions.size - 1)

            if (isLastQuestionOfLastSubject) {
                showSubmitDialog()
            } else if (currentQuestionIndexInSubject < currentSubjectQuestionList.size - 1) {
                // Move to next question in current subject
                stopCurrentQuestionTimer()
                currentQuestionIndexInSubject++
                questionsRv.scrollToPosition(currentQuestionIndexInSubject)
                updateQuestionDisplay()
                updateMarkReviewButtonUI() // Update review button state for the new question
                startCurrentQuestionTimer()
            } else {
                // Last question of current subject, try to move to next subject
                val currentSubjectIndex = subjectNames.indexOf(currentSubject)
                if (currentSubjectIndex < subjectWiseQuestions.size - 1) {
                    val nextSubject = subjectNames[currentSubjectIndex + 1]
                    // onSubjectSelected will handle updating questionsRv and currentQuestionIndexInSubject to 0
                    onSubjectSelected(nextSubject)
                    subjectAdapter.setSelectedPosition(currentSubjectIndex + 1)
                    subjectsRv.scrollToPosition(currentSubjectIndex + 1)
                }
            }
            questionsRv.suppressLayout(true) // Resuppress layout after all operations
            updateNavigationButtonsVisibility()
        }

        prevQuesBtn.setOnClickListener {
            questionsRv.suppressLayout(false) // Unsuppress layout for any updates/scrolls
            if (currentQuestionIndexInSubject > 0) {
                // Move to previous question in current subject
                stopCurrentQuestionTimer()
                currentQuestionIndexInSubject--
                questionsRv.scrollToPosition(currentQuestionIndexInSubject)
                updateQuestionDisplay()
                updateMarkReviewButtonUI() // Update review button state for the new question
                startCurrentQuestionTimer()
            } else {
                // First question of the current subject, try to move to previous subject
                val subjectNames = ArrayList(subjectWiseQuestions.keys)
                val currentSubjectIndex = subjectNames.indexOf(currentSubject)
                if (currentSubjectIndex > 0) {
                    val prevSubject = subjectNames[currentSubjectIndex - 1]
                    stopCurrentQuestionTimer()
                    currentSubject = prevSubject
                    currentSubjectQuestionList = subjectWiseQuestions[prevSubject]!!
                    currentQuestionIndexInSubject = currentSubjectQuestionList.size - 1 // Set to the last question of previous subject

                    // Re-create and set adapter to force full redraw for the new subject's questions
                    questionsRv.suppressLayout(false) // Unfreeze before updating adapter
                    questionAdapter = QuestionAdapter(currentSubjectQuestionList, this)
                    questionsRv.adapter = questionAdapter
                    questionsRv.scrollToPosition(currentQuestionIndexInSubject)
                    questionsRv.suppressLayout(true) // Re-suppress after programmatic scroll completes

                    updateQuestionDisplay() // Update display for the new question
                    updateMarkReviewButtonUI() // Update review button state for the new question
                    startCurrentQuestionTimer()

                    subjectAdapter.setSelectedPosition(currentSubjectIndex - 1)
                    subjectsRv.scrollToPosition(currentSubjectIndex - 1)
                } else {
                    Toast.makeText(this, "First Question of First Subject", Toast.LENGTH_SHORT).show()
                }
            }
            questionsRv.suppressLayout(true) // Resuppress layout after all operations
            updateNavigationButtonsVisibility()
        }
    }

    private fun updateNavigationButtonsVisibility() {
        val subjectNames = ArrayList(subjectWiseQuestions.keys)
        val isFirstQuestionOfFirstSubject = (currentQuestionIndexInSubject == 0 && subjectNames.indexOf(currentSubject) == 0)
        prevQuesBtn.visibility = if (isFirstQuestionOfFirstSubject) View.INVISIBLE else View.VISIBLE

        val isLastQuestionOfLastSubject = (currentQuestionIndexInSubject == currentSubjectQuestionList.size - 1 &&
                subjectNames.indexOf(currentSubject) == subjectWiseQuestions.size - 1)

        if (isLastQuestionOfLastSubject) {
            nextButtonText.text = "Submit"
        } else {
            nextButtonText.text = "Next"
        }
    }

    private fun setupInfoAndBackButtons() {
        infoButton.setOnClickListener {
            val infoDialog = TestInfoDialogFragment()
            infoDialog.show(supportFragmentManager, "TestInfoDialog")
        }

        backButton.setOnClickListener {
            val warningDialog = TestWarningDialogFragment()
            warningDialog.show(supportFragmentManager, "TestWarningDialog")
        }
    }

    // New method to setup the mark review button
    private fun setupMarkReviewButton() {
        markReviewButton.setOnClickListener {
            if (currentSubjectQuestionList.isNotEmpty() && currentQuestionIndexInSubject < currentSubjectQuestionList.size) {
                val currentQuestion = currentSubjectQuestionList[currentQuestionIndexInSubject]
                currentQuestion.markedForReview = !currentQuestion.markedForReview // Toggle review status
                updateMarkReviewButtonUI() // Update UI
                Toast.makeText(this, if (currentQuestion.markedForReview) "Marked for Review" else "Removed from Review", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // New method to update the UI of the mark review button
    private fun updateMarkReviewButtonUI() {
        if (currentSubjectQuestionList.isNotEmpty() && currentQuestionIndexInSubject < currentSubjectQuestionList.size) {
            val currentQuestion = currentSubjectQuestionList[currentQuestionIndexInSubject]
            if (currentQuestion.markedForReview) {
                markReviewButton.setColorFilter(Color.parseColor("#8A2BE2")) // Purple tint
            } else {
                markReviewButton.setColorFilter(Color.parseColor("#555555")) // Default grey tint
            }
        }
    }

    // New method to setup the question menu button
    private fun setupQuestionMenuButton() {
        questionMenuButton.setOnClickListener {
            stopCurrentQuestionTimer() // Stop current question timer when going to menu
            val intent = Intent(this@TestMainActivity, TestQuestionMenuActivity::class.java)
            intent.putExtra("allQuestionList", allQuestionList as Serializable) // Pass all questions
            intent.putExtra("totalTimeLeftMillis", totalTimeLeftMillis) // Pass remaining total time
            questionMenuLauncher.launch(intent) // Use the launcher to start activity for result
        }
    }


    private fun updateQuestionDisplay() {
        if (currentSubjectQuestionList.isNotEmpty()) {
            // Calculate global question number
            var globalQuestionNumber = 0
            val subjectNames = ArrayList(subjectWiseQuestions.keys)
            for (subject in subjectNames) {
                if (subject == currentSubject) {
                    globalQuestionNumber += (currentQuestionIndexInSubject + 1)
                    break
                }
                globalQuestionNumber += subjectWiseQuestions[subject]!!.size
            }
            currQuesNo.text = String.format(Locale.getDefault(), "Q-%d", globalQuestionNumber)
            currPassedTime.text = formatCurrentQuestionTime(currentSubjectQuestionList[currentQuestionIndexInSubject].timeSpent)
        } else {
            currQuesNo.text = "Q-0"
            currPassedTime.text = "00:00"
        }
    }

    private fun startCurrentQuestionTimer() {
        if (currentQuestionTimeHandler == null) {
            currentQuestionTimeHandler = Handler()
        }
        if (currentQuestionTimeRunnable != null) {
            currentQuestionTimeHandler?.removeCallbacks(currentQuestionTimeRunnable!!)
        }

        currentQuestionTimeRunnable = Runnable {
            if (currentSubjectQuestionList.isNotEmpty() && currentQuestionIndexInSubject < currentSubjectQuestionList.size) {
                val currentQuestion = currentSubjectQuestionList[currentQuestionIndexInSubject]
                currentQuestion.timeSpent = currentQuestion.timeSpent + 1000 // Increment by 1 second
                currPassedTime.text = formatCurrentQuestionTime(currentQuestion.timeSpent)
            }
            currentQuestionTimeHandler?.postDelayed(currentQuestionTimeRunnable!!, 1000)
        }
        currentQuestionTimeHandler?.post(currentQuestionTimeRunnable!!)
    }

    private fun stopCurrentQuestionTimer() {
        if (currentQuestionTimeHandler != null && currentQuestionTimeRunnable != null) {
            currentQuestionTimeHandler?.removeCallbacks(currentQuestionTimeRunnable!!)
        }
    }

    private fun formatCurrentQuestionTime(millis: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(minutes)
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }

    // Method to show the submit dialog
    private fun showSubmitDialog() {
        stopCurrentQuestionTimer() // Stop the timer when the dialog is shown

        // Calculate statistics
        val totalQuestions = allQuestionList.size
        var answeredQuestions = 0
        var notAnsweredQuestions = 0
        var markedForReviewQuestions = 0

        for (question in allQuestionList) {
            if (question.selectedOptionIndex != -1) { // Assuming -1 means not answered
                answeredQuestions++
            } else {
                notAnsweredQuestions++
            }
            if (question.markedForReview) {
                markedForReviewQuestions++
            }
        }

        val submitDialog = TestSubmitDialogFragment.newInstance(
            totalTimeLeftMillis,
            totalQuestions,
            answeredQuestions,
            notAnsweredQuestions,
            markedForReviewQuestions
        )
        submitDialog.setOnSubmitClickListener(this) // Set the listener
        submitDialog.show(supportFragmentManager, "TestSubmitDialog")
    }

    override fun onDestroy() {
        super.onDestroy()
        totalCountDownTimer?.cancel()
        stopCurrentQuestionTimer()
    }

    override fun onOptionSelected(questionPosition: Int, selectedOptionIndex: Int) {
        questionsRv.suppressLayout(true)
    }

    override fun onOptionClick(questionPosition: Int, selectedOptionIndex: Int) {
        questionsRv.suppressLayout(false)
    }

    override fun onSubjectSelected(subjectName: String) {
        updateQuestionsForSelectedSubject(subjectName)
        updateNavigationButtonsVisibility()
    }

    override fun onSubmitConfirmed() {
        val intent = Intent(this, ResultMainActivity::class.java)
        startActivity(intent)
        finish() // For now, just finish the activity
    }
}