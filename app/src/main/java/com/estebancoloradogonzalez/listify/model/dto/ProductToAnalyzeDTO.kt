package com.estebancoloradogonzalez.listify.model.dto

import java.time.LocalDateTime

data class ProductToAnalyzeDTO(
    val id: Long,
    val name: String,
    val unitPrice: Double,
    val amount: Double,
    val unitOfMeasurement: String,
    val totalPrice: Double,
    val purchaseFrequency: String,
    val establishment: String,
    val category: String,
    val registrationDate: LocalDateTime,
)