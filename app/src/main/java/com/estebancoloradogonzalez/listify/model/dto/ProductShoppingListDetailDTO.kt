package com.estebancoloradogonzalez.listify.model.dto

data class ProductShoppingListDetailDTO(
    val id: Long,
    val unitPrice: Double,
    val purchasedAmount: Double,
    val unitOfMeasurementName: String
)
