package com.example.plant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class ArticlesAction : AppCompatActivity() {
        private lateinit var addnews : TextView
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_articles_action)
        addnews = findViewById(R.id.addnews)

        addnews.setOnClickListener{
            startActivity(Intent(this@ArticlesAction, AddNewsArticlesAction::class.java))
        }
    }
}