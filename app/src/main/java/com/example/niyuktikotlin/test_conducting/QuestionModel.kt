package com.example.niyuktikotlin.test_conducting

import java.io.Serializable

class QuestionModel(
    val questionText: String,
    val options: Array<String>,
    val correctOptionIndex: Int, // 0-indexed
    val subject: String
) : Serializable {

    var timeSpent: Long = 0 // in milliseconds
    var selectedOptionIndex: Int = -1 // -1 indicates no option selected yet
    var markedForReview: Boolean = false
}