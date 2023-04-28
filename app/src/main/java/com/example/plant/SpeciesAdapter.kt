package com.example.plant

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
    }

    override fun getItemCount(): Int {
        return speciesList.size
    }
}