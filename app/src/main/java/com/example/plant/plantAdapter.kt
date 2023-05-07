package com.example.plant

import android.content.Context
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class plantAdapter(private val plants: List<plant>,
                   private val context: Context ,
                   private val onPlantClickListener: OnPlantClickListener
) : RecyclerView.Adapter<plantAdapter.PlantViewHolder>() {

    interface OnPlantClickListener {
        fun onPlantClick(plant: plant)
    }

    class PlantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.name)
        val descriptionTextView: TextView = itemView.findViewById(R.id.description)
        val authorTextView: TextView = itemView.findViewById(R.id.author)
        val plantImageView: ImageView = itemView.findViewById(R.id.plantImage)
        val timestampTextView: TextView = itemView.findViewById(R.id.articleTimestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.plantitem, parent, false)
        return PlantViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        val plant = plants[position]
        holder.nameTextView.text = plant.name
        holder.descriptionTextView.text = plant.description
        holder.authorTextView.text = plant.author
        Glide.with(context)
            .load(plant.plantImage)
            .into(holder.plantImageView)
        holder.timestampTextView.text = DateFormat.format("dd/MM/yyyy", plant.createdAt.toDate())

        // Click event for each item
        holder.itemView.setOnClickListener {
            Toast.makeText(context, "Plant : ${plant.name}", Toast.LENGTH_SHORT).show()
            onPlantClickListener.onPlantClick(plant)
        }

    }



    override fun getItemCount(): Int {
        return plants.size
    }
}