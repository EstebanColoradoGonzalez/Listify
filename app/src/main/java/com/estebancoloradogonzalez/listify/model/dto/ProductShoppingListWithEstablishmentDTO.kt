package com.estebancoloradogonzalez.listify.model.dto

data class ProductShoppingListWithEstablishmentDTO(
    val productShoppingListId: Long,
    val productName: String,
    val amountValue: Double,
    val unitSymbol: String,
    val unitPrice: Double,
    val isReady: Boolean
)