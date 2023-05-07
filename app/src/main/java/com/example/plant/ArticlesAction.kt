package com.example.plant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp;
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ArticlesAction : AppCompatActivity(), ArticleAdapter.OnArticleClickListener{
    private lateinit var articlesRecyclerView: RecyclerView
    private lateinit var articleAdapter: ArticleAdapter
    private val articleList: MutableList<Article> = mutableListOf()

    private lateinit var addnews: TextView

    private fun fetchArticles() {
        val progressBar: ProgressBar = findViewById(R.id.progressBar)

// Trước khi tải dữ liệu
        progressBar.visibility = View.VISIBLE
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
                            val createdAt = document.getTimestamp("createdAt") ?: Timestamp.now()
                            val avatar = userDocument.getString("photoURL") ?: ""
                            val email = userDocument.getString("email") ?: ""
                            val article = Article(avatar, author, email ,title, description, imageUrl, createdAt  )
                            articleList.add(article)
                            articleAdapter.notifyDataSetChanged()
                        }
                    // After data loaded
                    progressBar.visibility = View.GONE
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
        articleAdapter = ArticleAdapter(articleList, this, this)
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
    override fun onArticleClick(article: Article) {
        val intent = Intent(this, DetailArticle_Action::class.java)
        intent.putExtra("article", article)
        startActivity(intent)
    }

}