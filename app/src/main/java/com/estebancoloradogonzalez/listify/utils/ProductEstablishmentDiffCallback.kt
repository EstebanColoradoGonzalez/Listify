package com.estebancoloradogonzalez.listify.utils

import androidx.recyclerview.widget.DiffUtil
import com.estebancoloradogonzalez.listify.model.dto.ProductShoppingListWithEstablishmentDTO

class ProductEstablishmentDiffCallback(
    private val oldList: List<ProductShoppingListWithEstablishmentDTO>,
    private val newList: List<ProductShoppingListWithEstablishmentDTO>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].productShoppingListId == newList[newItemPosition].productShoppingListId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
