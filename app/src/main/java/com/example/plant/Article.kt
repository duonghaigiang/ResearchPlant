package com.example.plant
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

import com.google.firebase.Timestamp;
@Parcelize
data class Article(
    val id: String = "",
    val avatar : String = "",
    val author: String = "",
    val email : String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    var likes: MutableList<String> = mutableListOf()
): Parcelable