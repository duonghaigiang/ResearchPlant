package com.example.plant

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class SpeciesAction : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var speciesAdapter: SpeciesAdapter
    private lateinit var addnews : TextView
    private val speciesList: MutableList<Species> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_species_action)
        recyclerView = findViewById(R.id.speciesRecyclerView)

        // Initialize addnews before setting OnClickListener
        addnews = findViewById(R.id.addnews)  // Replace with your actual TextView id
        addnews.setOnClickListener {
            val intent = Intent(this, AddNewSpeciesAction::class.java)
            startActivity(intent)
        }

        speciesAdapter = SpeciesAdapter(speciesList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = speciesAdapter

        fetchSpecies()
    }

    private fun fetchSpecies() {
        val progressBar: ProgressBar = findViewById(R.id.progressBar)

// Trước khi tải dữ liệu
        progressBar.visibility = View.VISIBLE
        val db = FirebaseFirestore.getInstance()
        db.collection("species")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val speciesName = document.getString("name") ?: ""
                    val species = Species(speciesName)
                    speciesList.add(species)
                    speciesAdapter.notifyDataSetChanged()
                }
                progressBar.visibility = View.GONE
            }
            .addOnFailureListener { exception ->
                Log.d("SpeciesAction", "Error fetching species: ", exception)
                progressBar.visibility = View.GONE
            }
    }
}

