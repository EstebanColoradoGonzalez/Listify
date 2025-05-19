package com.estebancoloradogonzalez.listify.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.estebancoloradogonzalez.listify.R
import com.estebancoloradogonzalez.listify.model.dto.ProductShoppingListWithEstablishmentDTO

class ProductEstablishmentAdapter(
    private var products: List<ProductShoppingListWithEstablishmentDTO>,
    private val onItemClick: (ProductShoppingListWithEstablishmentDTO) -> Unit,
    private val onReadyChange: (productId: Long, isReady: Boolean) -> Unit,
    private var isActive: Boolean = true
) : RecyclerView.Adapter<ProductEstablishmentAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardViewProduct: CardView = itemView.findViewById(R.id.cardViewProduct)
        val cbIsReady: CheckBox = itemView.findViewById(R.id.cbIsReady)
        val tvProductName: TextView = itemView.findViewById(R.id.tvProductName)
        val tvAmountValue: TextView = itemView.findViewById(R.id.tvAmountValue)
        val tvUnitPrice: TextView = itemView.findViewById(R.id.tvUnitPrice)
        val tvUnitSymbol: TextView = itemView.findViewById(R.id.tvUnitSymbol)

        fun bind(product: ProductShoppingListWithEstablishmentDTO) {
            cbIsReady.setOnCheckedChangeListener(null)
            cbIsReady.isChecked = product.isReady

            tvProductName.text = product.productName
            tvAmountValue.text = product.amountValue.toString()
            tvUnitPrice.text = product.unitPrice.toString()
            tvUnitSymbol.text = product.unitSymbol

            val context = cardViewProduct.context
            val color = if (product.isReady)
                context.getColor(R.color.light_gray)
            else
                context.getColor(R.color.item_green)
            cardViewProduct.setCardBackgroundColor(color)

            cbIsReady.isEnabled = isActive
            itemView.isEnabled = isActive
            cardViewProduct.isEnabled = isActive

            val alpha = if (isActive) 1.0f else 0.75f
            cardViewProduct.alpha = alpha
            cbIsReady.alpha = alpha
            tvProductName.alpha = alpha
            tvAmountValue.alpha = alpha
            tvUnitPrice.alpha = alpha
            tvUnitSymbol.alpha = alpha

            if (isActive) {
                cbIsReady.setOnCheckedChangeListener { _, isChecked ->
                    onReadyChange(product.productShoppingListId, isChecked)
                }
                itemView.setOnClickListener { onItemClick(product) }
            } else {
                cbIsReady.setOnCheckedChangeListener(null)
                itemView.setOnClickListener(null)
            }
        }
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    fun updateProducts(newProducts: List<ProductShoppingListWithEstablishmentDTO>) {
        this.products = newProducts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_establishment, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int = products.size
}


