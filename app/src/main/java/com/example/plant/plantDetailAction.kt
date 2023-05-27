package com.example.plant

import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class plantDetailAction  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_detail_action)

        val NumLike: TextView = findViewById(R.id.NumLike)
        val likeicon: ImageButton = findViewById(R.id.likeicon)
        val plant = intent.getParcelableExtra<plant>("plant")
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
        plant?.let {
            Glide.with(this)
                .load(it.plantImage)
                .into(imagePlant)
            titleTextView.text = "Name: ${it.name}\nSpecies: ${it.species}"
            descriptionTextView.text = "Descriptions : ${it.description}"
            authorTextView.text = "Author : ${it.author}"
            email.text = "Email : ${it.email}"
            timestampTextView.text = DateFormat.format("dd/MM/yyyy", it.createdAt.toDate())
            Glide.with(this)
                .load(it.avartar)
                .into(imgAvatar)
            updateLikeIcon(likeicon, it , FirebaseAuth.getInstance().currentUser?.uid)
            updateNumLikes(it, NumLike)
        }

        likeicon.setOnClickListener {
            plant?.let { plant ->
                toggleLike(plant, likeicon)
            }
        }

    }
    private fun toggleLike(plant: plant, likeicon: ImageButton) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        currentUserId?.let { uid ->
            val db = FirebaseFirestore.getInstance()
            val plantRef = db.collection("plants").document(plant.id)

            if (plant.likes?.contains(uid) == true) {
                plantRef.update("likes", FieldValue.arrayRemove(uid)).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        plant.likes?.remove(uid)
                        updateLikeIcon(likeicon, plant, uid)
                    } else {
                        Toast.makeText(this, "Failed to update like status: ${task.exception}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                plantRef.update("likes", FieldValue.arrayUnion(uid)).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        plant.likes?.add(uid)
                        updateLikeIcon(likeicon, plant, uid)
                    } else {
                        Toast.makeText(this, "Failed to update like status: ${task.exception}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    private fun updateLikeIcon(likeIcon: ImageButton, plant: plant, currentUserId: String?) {
        val db = FirebaseFirestore.getInstance()
        val plantRef = db.collection("plants").document(plant.id)

        plantRef.get().addOnSuccessListener { documentSnapshot ->
            val fetchedPlant = documentSnapshot.toObject(plant::class.java)
            fetchedPlant?.let {
                val liked = it.likes?.contains(currentUserId) ?: false
                if (liked) {
                    likeIcon.setImageResource(R.drawable.liked_icon) // Replace with your own liked icon
                } else {
                    likeIcon.setImageResource(R.drawable.like_icon) // Replace with your own unliked icon
                }
                Log.d("updateLikeIcon", "Like icon updated successfully for plant: ${plant.id}")
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this, "Failed to fetch likes: $exception", Toast.LENGTH_SHORT).show()
            Log.e("updateLikeIcon", "Failed to update like icon for plant: ${plant.id}", exception)
        }

    }
    private fun updateNumLikes(plant: plant, numLikesTextView: TextView) {
        val db = FirebaseFirestore.getInstance()
        val plantRef = db.collection("plants").document(plant.id)

        plantRef.get().addOnSuccessListener { documentSnapshot ->
            val fetchedArticle = documentSnapshot.toObject(Article::class.java)
            fetchedArticle?.let {
                val numOfLikes = it.likes?.size ?: 0
                numLikesTextView.text = numOfLikes.toString()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this, "Failed to fetch likes: $exception", Toast.LENGTH_SHORT).show()
        }
    }




}