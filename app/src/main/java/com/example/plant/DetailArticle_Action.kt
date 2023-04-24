package com.example.plant
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.text.format.DateFormat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class DetailArticle_Action  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail)
        val currentUser = Firebase.auth.currentUser

        //mapping
        val article = intent.getParcelableExtra<Article>("article")

        val imgAvatar: ImageView = findViewById(R.id.imgAvatar)

        val imagePlant: ImageView = findViewById(R.id.image)
        val titleTextView: TextView = findViewById(R.id.title)
        val email: TextView = findViewById(R.id.mail)
        val descriptionTextView: TextView = findViewById(R.id.description)
        val authorTextView: TextView = findViewById(R.id.name)
        val timestampTextView: TextView = findViewById(R.id.date)

        val currentUserTextView = email
        currentUser?.let {
            currentUserTextView.text = "Email: ${it.email}"
        }


        article?.let {
            Glide.with(this)
                .load(it.imageUrl)
                .into(imagePlant)
            titleTextView.text = "Title : ${it.title}"
            descriptionTextView.text = "Descriptions : ${it.description}"
            authorTextView.text = "Name : ${it.author}"
            timestampTextView.text = DateFormat.format("dd/MM/yyyy", it.createdAt.toDate())
            Glide.with(this)
                .load(it.avatar)
                .into(imgAvatar)
        }
    }

    private fun mapping()
    {

    }

}