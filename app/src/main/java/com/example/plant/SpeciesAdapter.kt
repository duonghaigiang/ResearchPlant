package com.example.plant

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.View;
class SpeciesAdapter(private val speciesList: List<Species>) :
    RecyclerView.Adapter<SpeciesAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val speciesName: TextView = itemView.findViewById(R.id.species)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.species_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val species = speciesList[position]
        holder.speciesName.text = species.speciesName
        // Set the OnClickListener
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, SpeciesPlantAction::class.java)
            intent.putExtra("speciesName", species.speciesName)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return speciesList.size
    }
}