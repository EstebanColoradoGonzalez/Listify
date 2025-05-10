package com.estebancoloradogonzalez.listify.model.dto

import java.time.LocalDateTime

data class ShoppingListDTO(
    val id: Long,
    val date: LocalDateTime,
    val status: String,
)
