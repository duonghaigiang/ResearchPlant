package com.example.plant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentUser = Firebase.auth.currentUser // viết tắt của kotlin
        val currentUserTextView = findViewById<TextView>(R.id.currentUser)
        currentUser?.let {
            currentUserTextView.text = it.email
        }
        // Sử dụng biến currentUser cho các tác vụ của bạn
    }
}