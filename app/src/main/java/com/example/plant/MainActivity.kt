package com.example.plant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var username : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        username = findViewById(R.id.username)
        val currentUser = Firebase.auth.currentUser // viết tắt của kotlin
        val currentUserTextView = username
        currentUser?.let {
            currentUserTextView.text = it.displayName
        }
        // Sử dụng biến currentUser cho các tác vụ của bạn
    }
}