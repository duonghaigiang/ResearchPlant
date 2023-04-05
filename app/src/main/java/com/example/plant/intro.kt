package com.example.plant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class intro : AppCompatActivity() {
    private lateinit var btn_Intro : Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.intro)

        btn_Intro = findViewById(R.id.btn_Intro)
        btn_Intro.setOnClickListener {
            startActivity(Intent(this@intro, LoginAction::class.java))
        }
    }
}