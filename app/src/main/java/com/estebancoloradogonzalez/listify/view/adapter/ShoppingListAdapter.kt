package com.estebancoloradogonzalez.listify.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.estebancoloradogonzalez.listify.R
import com.estebancoloradogonzalez.listify.model.dto.ShoppingListDTO
import com.estebancoloradogonzalez.listify.utils.TextConstants
import java.time.format.DateTimeFormatter

class ShoppingListAdapter(
    private val shoppingLists: List<ShoppingListDTO>,
    private val onItemClick: (ShoppingListDTO) -> Unit
) : RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder>() {
    class ShoppingListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.cardViewShoppingList)
        val tvShoppingListDate: TextView = itemView.findViewById(R.id.tvShoppingListDate)
        val tvShoppingListStatus: TextView = itemView.findViewById(R.id.tvShoppingListStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shopping_list, parent, false)
        return ShoppingListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        val shoppingList = shoppingLists[position]
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(TextConstants.DATE_FORMAT)
        val dateFormatted = shoppingList.date.format(formatter)
        holder.tvShoppingListDate.text = dateFormatted

        holder.tvShoppingListStatus.text = shoppingList.status

        holder.cardView.setOnClickListener {
            onItemClick(shoppingList)
        }
    }

    override fun getItemCount(): Int = shoppingLists.size
}