package com.estebancoloradogonzalez.listify.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.estebancoloradogonzalez.listify.R
import com.estebancoloradogonzalez.listify.model.dto.EstablishmentNameDTO
import android.widget.TextView

class EstablishmentAdapter(
    private val establishments: List<EstablishmentNameDTO>,
    private val onClick: (EstablishmentNameDTO) -> Unit
) : RecyclerView.Adapter<EstablishmentAdapter.EstablishmentViewHolder>() {

    inner class EstablishmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvEstablishmentName: TextView = itemView.findViewById(R.id.tvEstablishmentName)

        fun bind(establishment: EstablishmentNameDTO) {
            tvEstablishmentName.text = establishment.name
            itemView.setOnClickListener {
                onClick(establishment)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstablishmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_establishment, parent, false)
        return EstablishmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: EstablishmentViewHolder, position: Int) {
        holder.bind(establishments[position])
    }

    override fun getItemCount(): Int = establishments.size
}
