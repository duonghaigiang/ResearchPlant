package com.example.plant

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class plant(
    val id: String = "",
    val name: String = "",
    val author : String = "",
    val description : String = "",
    val createdAt: Timestamp = Timestamp.now(),
    val plantImage :String = "",
    val avartar : String = "",
    val email : String = "",
    val species : String = "",
    var likes: MutableList<String> = mutableListOf()
): Parcelable