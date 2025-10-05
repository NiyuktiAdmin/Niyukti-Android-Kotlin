package com.example.niyuktikotlin.models

data class CourseModel(
    val title: String,
    val actualPrice: Int,
    val discountedPrice: Int,
    val discountPercent: Int,
    val imageResId: Int,
    val category: String
)