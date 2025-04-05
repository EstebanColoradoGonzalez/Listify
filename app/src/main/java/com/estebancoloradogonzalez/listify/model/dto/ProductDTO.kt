package com.estebancoloradogonzalez.listify.model.dto

data class ProductDTO(
    val id: Long,
    val name: String,
    val unitPrice: Double,
    val unitSymbol: String
)