package com.example.plant

import com.google.firebase.Timestamp

data class plant(
    val name: String ,
    val author : String,
    val description : String,
    val createdAt: Timestamp,
    val plantImage :String,
    )