package com.example.plant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AddNewsArticlesAction : AppCompatActivity() {

    private lateinit var username : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addnews_articles_action)
        username = findViewById(R.id.username)

        val currentUser = Firebase.auth.currentUser // viết tắt của kotlin

        val currentUserTextView = username
        currentUser?.let {
            currentUserTextView.text = it.displayName
        }
    }
}