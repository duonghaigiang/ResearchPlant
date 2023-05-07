package com.example.plant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp;

class SpeciesPlantAction : AppCompatActivity() , plantAdapter.OnPlantClickListener{
    private lateinit var recyclerView: RecyclerView
    private lateinit var plantAdapter: plantAdapter
    private val plantList: MutableList<plant> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speciesplant_action)
        recyclerView = findViewById(R.id.speciesRecyclerView)

        plantAdapter = plantAdapter(plantList, this, this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = plantAdapter

        // Get the species name from intent extra
        val speciesName = intent.getStringExtra("speciesName") ?: ""
        fetchPlantsBySpecies(speciesName)
    }

    private fun fetchPlantsBySpecies(speciesName: String) {

        val progressBar: ProgressBar = findViewById(R.id.progressBar)

// Trước khi tải dữ liệu
        progressBar.visibility = View.VISIBLE

// Tải dữ liệu
        val db = FirebaseFirestore.getInstance()
        db.collection("plants")
            .whereEqualTo("speciesId", speciesName)  // Assuming there's a field "speciesName" in plant document
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val uid = document.getString("uid") ?: ""
                    db.collection("users").document(uid)
                        .get()
                        .addOnSuccessListener { userDocument ->
                            val plantName = document.getString("name") ?: ""
                            val author = document.getString("author") ?: "?"
                            val description = document.getString("description") ?: ""
                            val createdAt = document.getTimestamp("createdAt") ?: Timestamp.now()
                            val plantImage = document.getString("imgURL") ?: ""
                            val avartar =userDocument.getString("photoURL") ?: ""
                            val email =userDocument.getString("email") ?: ""
                            val species = document.getString("speciesId") ?: ""
                            val plantId = document.id // Get the document ID
                            val plant = plant(plantId, plantName, author, description, createdAt, plantImage, avartar, email , species) // Pass the document ID to the plant object
                            plantList.add(plant)
                            plantAdapter.notifyDataSetChanged()
                        }
                    progressBar.visibility = View.GONE

                }
            }
            .addOnFailureListener { exception ->
                Log.d("SpeciesPlantAction", "Error fetching plants: ", exception)
                progressBar.visibility = View.GONE
            }
    }
    override fun onPlantClick(plant: plant) {
        val intent = Intent(this, plantDetailAction::class.java)
        intent.putExtra("plant", plant)
        startActivity(intent)

    }
}