package com.example.plant
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

import com.google.firebase.Timestamp;
@Parcelize
data class Article(
    val avatar : String,
    val author: String,
    val title: String,
    val description: String,
    val imageUrl: String,
      val createdAt: Timestamp
): Parcelable