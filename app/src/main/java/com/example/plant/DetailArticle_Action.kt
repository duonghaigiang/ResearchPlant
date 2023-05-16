package com.example.plant
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import io.grpc.InternalChannelz.id

class DetailArticle_Action  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail)
        val currentUser = Firebase.auth.currentUser


        //mapping
        val article = intent.getParcelableExtra<Article>("article")
        val likeicon: ImageButton = findViewById(R.id.likeicon)
        val imgAvatar: ImageView = findViewById(R.id.imgAvatar)
        val btnHome: Button = findViewById(R.id.btnHome)
        val imagePlant: ImageView = findViewById(R.id.image)
        val titleTextView: TextView = findViewById(R.id.title)
        val email: TextView = findViewById(R.id.mail)
        val descriptionTextView: TextView = findViewById(R.id.description)
        val authorTextView: TextView = findViewById(R.id.name)
        val timestampTextView: TextView = findViewById(R.id.date)


        btnHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        article?.let {
            Glide.with(this)
                .load(it.imageUrl)
                .into(imagePlant)
            titleTextView.text = "Title : ${it.title}"
            descriptionTextView.text = "Descriptions : ${it.description}"
            authorTextView.text = "Author : ${it.author}"
            email.text = "Email : ${it.email}"
            timestampTextView.text = DateFormat.format("dd/MM/yyyy", it.createdAt.toDate())
            Glide.with(this)
                .load(it.avatar)
                .into(imgAvatar)
            updateLikeIcon(likeicon, it , FirebaseAuth.getInstance().currentUser?.uid)
        }
        likeicon.setOnClickListener {
            article?.let { article ->
                toggleLike(article, likeicon)
            }
        }
    }

    private fun toggleLike(article: Article, likeicon: ImageButton) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        currentUserId?.let { uid ->
            val db = FirebaseFirestore.getInstance()
            val plantRef = db.collection("articles").document(article.id)

            if (article.likes?.contains(uid) == true) {
                plantRef.update("likes", FieldValue.arrayRemove(uid)).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        article.likes?.remove(uid)
                        updateLikeIcon(likeicon, article, uid)
                    } else {
                        Toast.makeText(this, "Failed to update like status: ${task.exception}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                plantRef.update("likes", FieldValue.arrayUnion(uid)).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        article.likes?.add(uid)
                        updateLikeIcon(likeicon, article, uid)
                    } else {
                        Toast.makeText(this, "Failed to update like status: ${task.exception}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    private fun updateLikeIcon(likeIcon: ImageButton, article: Article, currentUserId: String?) {

        val progressBar: ProgressBar = findViewById(R.id.progressBar)

// Trước khi tải dữ liệu
        progressBar.visibility = View.VISIBLE

        val db = FirebaseFirestore.getInstance()
        val plantRef = db.collection("articles").document(article.id)

        plantRef.get().addOnSuccessListener { documentSnapshot ->
            val fetchedPlant = documentSnapshot.toObject(plant::class.java)
            fetchedPlant?.let {
                val liked = it.likes?.contains(currentUserId) ?: false
                if (liked) {
                    likeIcon.setImageResource(R.drawable.liked_icon) // Replace with your own liked icon
                } else {
                    likeIcon.setImageResource(R.drawable.like_icon) // Replace with your own unliked icon
                }
                Log.d("updateLikeIcon", "Like icon updated successfully for plant: ${article.id}")
            }
            // After data loaded
            progressBar.visibility = View.GONE
        }.addOnFailureListener { exception ->
            Toast.makeText(this, "Failed to fetch likes: $exception", Toast.LENGTH_SHORT).show()
            Log.e("updateLikeIcon", "Failed to update like icon for plant: ${article.id}", exception)
        }

    }

}