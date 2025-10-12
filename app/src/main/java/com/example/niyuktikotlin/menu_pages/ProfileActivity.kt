package com.example.niyuktikotlin.menu_pages

import BaseActivity
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.niyuktikotlin.R

class ProfileActivity : BaseActivity() {

    // Using lateinit var for views that are initialized in initViews()
    private lateinit var basicInfoContent: LinearLayout
    private lateinit var addressInfoContent: LinearLayout
    private lateinit var personalInfoContent: LinearLayout
    private lateinit var educationalInfoContent: LinearLayout

    private lateinit var basicInfoArrow: ImageView
    private lateinit var addressInfoArrow: ImageView
    private lateinit var personalInfoArrow: ImageView
    private lateinit var educationalInfoArrow: ImageView

    private lateinit var basicInfoHeader: LinearLayout
    private lateinit var addressInfoHeader: LinearLayout
    private lateinit var personalInfoHeader: LinearLayout
    private lateinit var educationalInfoHeader: LinearLayout

    // Spinners
    private lateinit var stateSpinner: Spinner
    private lateinit var districtSpinner: Spinner
    private lateinit var citySpinner: Spinner
    private lateinit var genderSpinner: Spinner
    private lateinit var categorySpinner: Spinner
    private lateinit var qualificationSpinner: Spinner
    private lateinit var targetExamSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Using R.layout from the imported package
        setContentView(R.layout.activity_profile)

        initViews()
        setupSpinners()
        setupExpandableCards()
    }

    private fun initViews() {
        // Content layouts
        basicInfoContent = findViewById(R.id.basicInfoContent)
        addressInfoContent = findViewById(R.id.addressInfoContent)
        personalInfoContent = findViewById(R.id.personalInfoContent)
        educationalInfoContent = findViewById(R.id.educationalInfoContent)

        // Arrow icons
        basicInfoArrow = findViewById(R.id.basicInfoArrow)
        addressInfoArrow = findViewById(R.id.addressInfoArrow)
        personalInfoArrow = findViewById(R.id.personalInfoArrow)
        educationalInfoArrow = findViewById(R.id.educationalInfoArrow)

        // Header layouts (clickable)
        basicInfoHeader = findViewById(R.id.basicInfoHeader)
        addressInfoHeader = findViewById(R.id.addressInfoHeader)
        personalInfoHeader = findViewById(R.id.personalInfoHeader)
        educationalInfoHeader = findViewById(R.id.educationalInfoHeader)

        // Spinners
        stateSpinner = findViewById(R.id.stateSpinner)
        districtSpinner = findViewById(R.id.districtSpinner)
        citySpinner = findViewById(R.id.citySpinner)
        genderSpinner = findViewById(R.id.genderSpinner)
        categorySpinner = findViewById(R.id.categorySpinner)
        qualificationSpinner = findViewById(R.id.qualificationSpinner)
        targetExamSpinner = findViewById(R.id.targetExamSpinner)
    }

    private fun setupSpinners() {
        // State spinner
        val states = arrayOf("Maharashtra", "Punjab", "Karnataka", "Tamil Nadu", "Gujarat")
        setupSpinner(stateSpinner, states, 0)

        // District spinner
        val districts = arrayOf("Mumbai", "Pune", "Nashik", "Nagpur", "Aurangabad")
        setupSpinner(districtSpinner, districts, 0)

        // City spinner
        val cities = arrayOf("Andheri", "Bandra", "Colaba", "Dadar", "Fort")
        setupSpinner(citySpinner, cities, 0)

        // Gender spinner
        val genders = arrayOf("Male", "Female", "Other")
        setupSpinner(genderSpinner, genders, 0)

        // Category spinner
        val categories = arrayOf("General", "OBC", "SC", "ST")
        setupSpinner(categorySpinner, categories, 0)

        // Qualification spinner
        val qualifications = arrayOf("Graduate", "Post Graduate", "Diploma", "12th Pass", "10th Pass")
        setupSpinner(qualificationSpinner, qualifications, 0)

        // Target Exam spinner
        val targetExams = arrayOf("PSI", "UPSC", "MPSC", "SSC", "Banking")
        setupSpinner(targetExamSpinner, targetExams, 0)
    }

    private fun setupSpinner(spinner: Spinner, items: Array<String>, defaultSelection: Int) {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            items
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setSelection(defaultSelection)
    }

    private fun setupExpandableCards() {
        basicInfoHeader.setOnClickListener {
            toggleExpansion(basicInfoContent, basicInfoArrow)
        }

        addressInfoHeader.setOnClickListener {
            toggleExpansion(addressInfoContent, addressInfoArrow)
        }

        personalInfoHeader.setOnClickListener {
            toggleExpansion(personalInfoContent, personalInfoArrow)
        }

        educationalInfoHeader.setOnClickListener {
            toggleExpansion(educationalInfoContent, educationalInfoArrow)
        }
    }

    private fun toggleExpansion(contentLayout: LinearLayout, arrowIcon: ImageView) {
        if (contentLayout.visibility == View.GONE) {
            // Expand
            expand(contentLayout)
            rotateArrow(arrowIcon, 0f, 180f)
        } else {
            // Collapse
            collapse(contentLayout)
            rotateArrow(arrowIcon, 180f, 0f)
        }
    }

    private fun expand(contentLayout: LinearLayout) {
        contentLayout.visibility = View.VISIBLE

        // Measure the content layout
        contentLayout.measure(
            View.MeasureSpec.makeMeasureSpec(contentLayout.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )

        val targetHeight = contentLayout.measuredHeight

        // Set initial height to 0
        contentLayout.layoutParams.height = 0
        contentLayout.requestLayout()

        // Animate to target height
        val animator = ValueAnimator.ofInt(0, targetHeight)
        animator.duration = 300
        animator.interpolator = DecelerateInterpolator()
        animator.addUpdateListener { animation ->
            val layoutParams = contentLayout.layoutParams
            layoutParams.height = animation.animatedValue as Int
            contentLayout.layoutParams = layoutParams
        }
        animator.start()
    }

    private fun collapse(contentLayout: LinearLayout) {
        val initialHeight = contentLayout.height

        val animator = ValueAnimator.ofInt(initialHeight, 0)
        animator.duration = 300
        animator.interpolator = DecelerateInterpolator()
        animator.addUpdateListener { animation ->
            val layoutParams = contentLayout.layoutParams
            layoutParams.height = animation.animatedValue as Int
            contentLayout.layoutParams = layoutParams
        }

        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                contentLayout.visibility = View.GONE
                val layoutParams = contentLayout.layoutParams
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                contentLayout.layoutParams = layoutParams
            }
        })
        animator.start()
    }

    private fun rotateArrow(arrowIcon: ImageView, fromDegree: Float, toDegree: Float) {
        val rotation = RotateAnimation(
            fromDegree, toDegree,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 300
            fillAfter = true
        }
        arrowIcon.startAnimation(rotation)
    }
}