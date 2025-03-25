package com.estebancoloradogonzalez.listify.utils

object Queries {
    const val SELECT_USER = "SELECT * FROM user LIMIT 1"
    const val SELECT_BUDGET = "SELECT * FROM budget LIMIT 1"
    const val SELECT_USER_ID = "SELECT id FROM user LIMIT 1"
    const val UPDATE_USER = "UPDATE user SET name = :newName WHERE id = :userId"
    const val UPDATE_BUDGET = "UPDATE budget SET value = :newBudget WHERE id = :id"
}