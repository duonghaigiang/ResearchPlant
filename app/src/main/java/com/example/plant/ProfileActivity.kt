package com.example.plant

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity() {
    private lateinit var username : TextView

    private lateinit var emailUser : TextView
    private lateinit var btnHome : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activiti_profile_action)

        username = findViewById(R.id.nameuser)
        emailUser = findViewById(R.id.emailUser)
        btnHome = findViewById(R.id.btnHome)
        val currentUser = Firebase.auth.currentUser // viết tắt của kotlin

        currentUser?.let {
            username.text = it.displayName
        }
        currentUser?.let {
            emailUser.text = it.email
        }
        btnHome.setOnClickListener{
            startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
        }
    }
}