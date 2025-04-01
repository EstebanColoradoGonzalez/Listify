package com.estebancoloradogonzalez.listify.utils

object Queries {
    const val SELECT_USER = "SELECT * FROM user LIMIT 1"
    const val SELECT_BUDGET_ID = "SELECT id FROM budget LIMIT 1"
    const val SELECT_BUDGET = "SELECT * FROM budget LIMIT 1"
    const val SELECT_USER_ID = "SELECT id FROM user LIMIT 1"
    const val UPDATE_USER = "UPDATE user SET name = :newName WHERE id = :userId"
    const val UPDATE_BUDGET = "UPDATE budget SET value = :newBudget WHERE id = :id"
    const val SELECT_CATEGORIES = "SELECT * FROM category"
    const val SELECT_CATEGORY_BY_ID = "SELECT * FROM category WHERE id = :id"
    const val SELECT_CATEGORY_BY_NAME = "SELECT * FROM category WHERE name = :name"
    const val UPDATE_CATEGORY = "UPDATE category SET name = :newName WHERE id = :id"
}