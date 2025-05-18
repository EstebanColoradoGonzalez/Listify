package com.estebancoloradogonzalez.listify.model.database.service

import com.estebancoloradogonzalez.listify.model.database.AppDatabase
import com.estebancoloradogonzalez.listify.model.database.utils.UserData
import com.estebancoloradogonzalez.listify.model.entity.Budget
import com.estebancoloradogonzalez.listify.model.entity.User
import java.time.LocalDateTime

class UserRegistrationService(private val db: AppDatabase) {
    suspend fun registerTestUser(): Long {
        val budgetDao = db.budgetDao()
        val userDao = db.userDao()

        val newBudget = Budget(value = UserData.USER_BUDGET.toDouble())
        budgetDao.insertBudget(newBudget)
        val budgetId = budgetDao.getBudgetId()

        val newUser = User(
            name = UserData.USER_NAME,
            registrationDate = LocalDateTime.now(),
            budget = budgetId
        )
        userDao.insertUser(newUser)

        return userDao.getUserId()
    }
}