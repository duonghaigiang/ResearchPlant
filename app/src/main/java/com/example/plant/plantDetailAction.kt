package com.example.plant

import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class plantDetailAction  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_detail_action)
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

            updateLikeIcon(likeicon, it, FirebaseAuth.getInstance().currentUser?.uid)
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
            if (plant.likes.contains(uid)) {
                plant.likes.remove(uid)
            } else {
                plant.likes.add(uid)
            }
            updateLikeIcon(likeicon, plant, uid)
            updatePlantLikesInFirestore(plant, plant.likes)
        }
    }

    private fun updateLikeIcon(likeIcon: ImageButton, plant: plant, currentUserId: String?) {
        val liked = plant.likes.contains(currentUserId)
        if (liked) {
            likeIcon.setImageResource(R.drawable.liked_icon) // Replace with your own liked icon
        } else {
            likeIcon.setImageResource(R.drawable.like_icon) // Replace with your own unliked icon
        }
    }

    private fun updatePlantLikesInFirestore(plant: plant, likes: MutableList<String>) {
        val db = FirebaseFirestore.getInstance()
        val plantRef = db.collection("plants").document(plant.id)

        plantRef.update("likes", likes)
            .addOnSuccessListener {
                Toast.makeText(this, "Like status updated", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to update like status: $exception", Toast.LENGTH_SHORT).show()
            }
    }

}