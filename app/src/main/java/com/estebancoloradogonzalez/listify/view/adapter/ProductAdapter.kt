package com.estebancoloradogonzalez.listify.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.estebancoloradogonzalez.listify.R
import com.estebancoloradogonzalez.listify.model.dto.ProductDTO

class ProductAdapter(
    private val products: List<ProductDTO>,
    private val onItemClick: (ProductDTO) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.cardViewProduct)
        val tvProductName: TextView = itemView.findViewById(R.id.tvProductName)
        val tvProductPriceUnit: TextView = itemView.findViewById(R.id.tvProductPriceUnit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.tvProductName.text = product.name
        holder.tvProductPriceUnit.text = "$${product.unitPrice} ${product.unitSymbol}"

        holder.cardView.setOnClickListener {
            onItemClick(product)
        }
    }

    override fun getItemCount() = products.size
}
