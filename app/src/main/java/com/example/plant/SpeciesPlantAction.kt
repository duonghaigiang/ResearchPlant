package com.example.plant

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp;

class SpeciesPlantAction : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var plantAdapter: plantAdapter
    private val plantList: MutableList<plant> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speciesplant_action)
        recyclerView = findViewById(R.id.speciesRecyclerView)

        plantAdapter = plantAdapter(plantList, this, object : plantAdapter.OnPlantClickListener {
            override fun onPlantClick(plant: plant) {
                // Handle plant click event here
            }
        })
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = plantAdapter

        // Get the species name from intent extra
        val speciesName = intent.getStringExtra("speciesName") ?: ""
        fetchPlantsBySpecies(speciesName)
    }

    private fun fetchPlantsBySpecies(speciesName: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("plants")
            .whereEqualTo("speciesId", speciesName)  // Assuming there's a field "speciesName" in plant document
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val plantName = document.getString("name") ?: ""
                    val author = document.getString("author") ?: "?"
                    val description = document.getString("description") ?: ""
                    val createdAt = document.getTimestamp("createdAt") ?: Timestamp.now()
                    val plantImage = document.getString("imgURL") ?: ""
                    val plant = plant(plantName, author, description, createdAt, plantImage)
                    plantList.add(plant)
                    plantAdapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener { exception ->
                Log.d("SpeciesPlantAction", "Error fetching plants: ", exception)
            }
    }
}