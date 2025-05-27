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
        private val cardViewProduct: CardView = itemView.findViewById(R.id.cardViewProduct)
        private val cbIsReady: CheckBox = itemView.findViewById(R.id.cbIsReady)
        private val tvProductName: TextView = itemView.findViewById(R.id.tvProductName)
        private val tvAmountValue: TextView = itemView.findViewById(R.id.tvAmountValue)
        private val tvUnitPrice: TextView = itemView.findViewById(R.id.tvUnitPrice)
        private val tvUnitSymbol: TextView = itemView.findViewById(R.id.tvUnitSymbol)

        fun bind(product: ProductShoppingListWithEstablishmentDTO) {
            bindProductInfo(product)
            updateUIState(product)
            setupListeners(product)
        }

        private fun bindProductInfo(product: ProductShoppingListWithEstablishmentDTO) {
            tvProductName.text = product.productName
            tvAmountValue.text = product.amountValue.toString()
            tvUnitPrice.text = product.unitPrice.toString()
            tvUnitSymbol.text = product.unitSymbol
            cbIsReady.setOnCheckedChangeListener(null)
            cbIsReady.isChecked = product.isReady
        }

        private fun updateUIState(product: ProductShoppingListWithEstablishmentDTO) {
            val context = cardViewProduct.context
            val bgColor = if (product.isReady) context.getColor(R.color.light_gray) else context.getColor(R.color.item_green)
            cardViewProduct.setCardBackgroundColor(bgColor)

            val alpha = if (isActive) 1.0f else 0.75f
            listOf(cardViewProduct, cbIsReady, tvProductName, tvAmountValue, tvUnitPrice, tvUnitSymbol).forEach {
                it.alpha = alpha
                it.isEnabled = isActive
            }
            itemView.isEnabled = isActive
        }

        private fun setupListeners(product: ProductShoppingListWithEstablishmentDTO) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_establishment, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size

    fun updateProducts(newProducts: List<ProductShoppingListWithEstablishmentDTO>) {
        this.products = newProducts
        notifyDataSetChanged()
    }
}