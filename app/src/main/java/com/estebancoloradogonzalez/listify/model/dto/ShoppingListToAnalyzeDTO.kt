package com.estebancoloradogonzalez.listify.model.dto

import java.time.LocalDateTime

data class ShoppingListToAnalyzeDTO(
    val id: Long,
    val date: LocalDateTime,
    val status: String,
    val products: List<ProductToAnalyzeDTO>
)