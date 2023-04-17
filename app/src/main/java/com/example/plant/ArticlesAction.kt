package com.example.plant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp;
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ArticlesAction : AppCompatActivity() {
    private lateinit var articlesRecyclerView: RecyclerView
    private lateinit var articleAdapter: ArticleAdapter
    private val articleList: MutableList<Article> = mutableListOf()

    private lateinit var addnews: TextView

    private fun fetchArticles() {
        val db = FirebaseFirestore.getInstance()
        val auth = Firebase.auth
        db.collection("articles")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val uid = document.getString("uid") ?: ""
                    db.collection("users").document(uid)
                        .get()
                        .addOnSuccessListener { userDocument ->
                            val author = userDocument.getString("displayName") ?: ""
                            val title = document.getString("title") ?: ""
                            val description = document.getString("description") ?: ""
                            val imageUrl = document.getString("imgURL") ?: ""
                            val timestamp = document.getTimestamp("timestamp") ?: Timestamp.now()
                            val article = Article(author, title, description, imageUrl, timestamp)
                            articleList.add(article)
                            articleAdapter.notifyDataSetChanged()
                        }
                }
                articleAdapter.notifyDataSetChanged()
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_articles_action)

        mapping()

        // Initialize the RecyclerView, LinearLayoutManager, and ArticleAdapter
        articlesRecyclerView = findViewById(R.id.articlesRecyclerView)
        val layoutManager = LinearLayoutManager(this)
        articleAdapter = ArticleAdapter(articleList, this)

        // Set up the RecyclerView with the adapter and layout manager
        articlesRecyclerView.adapter = articleAdapter
        articlesRecyclerView.layoutManager = layoutManager

        addnews.setOnClickListener{
            startActivity(Intent(this, AddNewsArticlesAction::class.java))
        }

        // Fetch the articles from Firestore
        fetchArticles()
    }
    private fun mapping()
    {
        addnews = findViewById(R.id.addnews)
    }
}