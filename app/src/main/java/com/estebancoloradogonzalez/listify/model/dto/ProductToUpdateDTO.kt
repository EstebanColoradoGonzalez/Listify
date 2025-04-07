package com.estebancoloradogonzalez.listify.model.dto

data class ProductToUpdateDTO(
    val id: Long,
    val name: String,
    val unitPrice: Double,
    val amount: Double,
    val unitOfMeasurement: String,
    val purchaseFrequency: String,
    val establishment: String,
    val category: String,
    val isActive: Boolean
)
