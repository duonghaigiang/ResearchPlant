package com.example.plant

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class plant(
    val name: String ,
    val author : String,
    val description : String,
    val createdAt: Timestamp,
    val plantImage :String,
    val avartar : String,
    val email : String,
    ): Parcelable