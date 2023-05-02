package com.example.plant

import android.os.Bundle
import android.text.format.DateFormat
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class plantDetailAction  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_detail_action)
        val plant = intent.getParcelableExtra<plant>("plant")

        val imgAvatar: ImageView = findViewById(R.id.imgAvatar)

        val imagePlant: ImageView = findViewById(R.id.image)
        val titleTextView: TextView = findViewById(R.id.title)
        val email: TextView = findViewById(R.id.mail)
        val descriptionTextView: TextView = findViewById(R.id.description)
        val authorTextView: TextView = findViewById(R.id.name)
        val timestampTextView: TextView = findViewById(R.id.date)
        plant?.let {
            Glide.with(this)
                .load(it.plantImage)
                .into(imagePlant)
            titleTextView.text = "Name : ${it.name}"
            descriptionTextView.text = "Descriptions : ${it.description}"
            authorTextView.text = "Author : ${it.author}"
            email.text = "Email : ${it.email}"
            timestampTextView.text = DateFormat.format("dd/MM/yyyy", it.createdAt.toDate())
            Glide.with(this)
                .load(it.avartar)
                .into(imgAvatar)
        }
    }
}