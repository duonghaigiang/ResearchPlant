package com.example.plant
import com.google.firebase.Timestamp;
data class Article(
    val author: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val timestamp: Timestamp
)