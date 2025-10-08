package com.example.niyuktikotlin.models

data class OfferModel(
    val id: String, // Appwrite document ID ($id)
    val courseId: String,
    val imageUrl: String // Full URL of the banner image
)