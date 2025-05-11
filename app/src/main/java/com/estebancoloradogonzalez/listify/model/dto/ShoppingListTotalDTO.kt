package com.estebancoloradogonzalez.listify.model.dto

import java.time.LocalDateTime

data class ShoppingListTotalDTO(
    val shoppingListDate: LocalDateTime,
    val totalAmount: Double
)