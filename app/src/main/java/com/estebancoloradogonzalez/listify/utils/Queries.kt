package com.estebancoloradogonzalez.listify.utils

object Queries {
    const val SELECT_USER = "SELECT * FROM user LIMIT 1"
    const val SELECT_BUDGET_ID = "SELECT id FROM budget LIMIT 1"
    const val SELECT_BUDGET = "SELECT * FROM budget LIMIT 1"
    const val SELECT_USER_ID = "SELECT id FROM user LIMIT 1"
    const val UPDATE_USER = "UPDATE user SET name = :newName WHERE id = :userId"
    const val UPDATE_BUDGET = "UPDATE budget SET value = :newBudget WHERE id = :id"
    const val SELECT_CATEGORIES = "SELECT * FROM category"
    const val SELECT_ESTABLISHMENTS = "SELECT * FROM establishment"
    const val SELECT_PURCHASE_FREQUENCY = "SELECT * FROM purchase_frequency"
    const val SELECT_UNIT_OF_MEASUREMENT = "SELECT * FROM unit_of_measurement"
    const val SELECT_STATE = "SELECT * FROM state"
    const val SELECT_CATEGORY_BY_ID = "SELECT * FROM category WHERE id = :id"
    const val SELECT_CATEGORY_BY_NAME = "SELECT * FROM category WHERE name = :name"
    const val UPDATE_CATEGORY = "UPDATE category SET name = :newName WHERE id = :id"
    const val DELETE_CATEGORY = "DELETE FROM category WHERE id = :id"
    const val DELETE_ESTABLISHMENTS = "DELETE FROM establishment"
    const val DELETE_PURCHASE_FREQUENCIES = "DELETE FROM purchase_frequency"
    const val DELETE_STATES = "DELETE FROM state"
    const val DELETE_UNITS_OF_MEASUREMENT = "DELETE FROM unit_of_measurement"
}