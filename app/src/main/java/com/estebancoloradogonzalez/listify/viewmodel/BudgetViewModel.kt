package com.estebancoloradogonzalez.listify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.estebancoloradogonzalez.listify.model.database.AppDatabase
import com.estebancoloradogonzalez.listify.model.entity.Budget
import com.estebancoloradogonzalez.listify.utils.InputValidator
import com.estebancoloradogonzalez.listify.utils.Messages
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BudgetViewModel(application: Application) : AndroidViewModel(application) {
    private val budgetDao = AppDatabase.getDatabase(application).budgetDao()

    suspend fun fetchBudget(): Budget? = withContext(Dispatchers.IO) {
        budgetDao.get()
    }

    fun updateBudgetAmount(id: Long, budgetAmount: String, onError: (String) -> Unit) {
        if (!isBudgetValid(budgetAmount)) {
            onError(Messages.ENTER_VALID_BUDGET_MESSAGE)
            return
        }
        updateBudgetInDatabase(id, budgetAmount.toDouble())
    }

    private fun isBudgetValid(budget: String): Boolean {
        return InputValidator.isValidNumericValue(budget)
    }

    private fun updateBudgetInDatabase(id: Long, amount: Double) {
        viewModelScope.launch {
            budgetDao.updateById(id, amount)
        }
    }
}