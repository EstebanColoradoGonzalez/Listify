package com.estebancoloradogonzalez.listify.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.estebancoloradogonzalez.listify.R
import com.estebancoloradogonzalez.listify.model.dto.ProductShoppingListWithEstablishmentDTO

class ProductEstablishmentAdapter(
    private val products: List<ProductShoppingListWithEstablishmentDTO>,
    private val onClick: (ProductShoppingListWithEstablishmentDTO) -> Unit
) : RecyclerView.Adapter<ProductEstablishmentAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cbIsReady: CheckBox = itemView.findViewById(R.id.cbIsReady)
        private val tvProductName: TextView = itemView.findViewById(R.id.tvProductName)
        private val tvAmountValue: TextView = itemView.findViewById(R.id.tvAmountValue)
        private val tvUnitPrice: TextView = itemView.findViewById(R.id.tvUnitPrice)
        private val tvUnitSymbol: TextView = itemView.findViewById(R.id.tvUnitSymbol)

        fun bind(product: ProductShoppingListWithEstablishmentDTO) {
            cbIsReady.isChecked = product.isReady
            tvProductName.text = product.productName
            tvAmountValue.text = product.amountValue.toString()
            tvUnitPrice.text = product.unitPrice.toString()
            tvUnitSymbol.text = product.unitSymbol

            itemView.setOnClickListener { onClick(product) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_establishment, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size
}
