package com.estebancoloradogonzalez.listify.model.dto

data class ActiveProductWithAmountDTO(
    val productId: Long,
    val productName: String,
    val unitPrice: Double,
    val amountId: Long,
    val amountValue: Double
)
