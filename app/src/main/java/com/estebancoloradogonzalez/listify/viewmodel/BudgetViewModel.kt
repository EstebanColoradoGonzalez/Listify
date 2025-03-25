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

    fun insertBudget(value: String, id: Long, onError: (String) -> Unit) {
        if (!InputValidator.isValidBudget(value)) {
            onError(Messages.ENTER_VALID_BUDGET_MESSAGE)
            return
        }

        viewModelScope.launch {
            val newBudget = Budget(value = value.toDouble(), id = id)
            budgetDao.insertBudget(newBudget)
        }
    }

    fun updateBudget(userId: Long, newBudget: String, onError: (String) -> Unit) {
        if (!InputValidator.isValidBudget(newBudget)) {
            onError(Messages.ENTER_VALID_BUDGET_MESSAGE)
            return
        }
        viewModelScope.launch { budgetDao.updateBudget(userId, newBudget.toDouble()) }
    }

    suspend fun getUserBudget(): Budget? {
        return withContext(Dispatchers.IO) {
            budgetDao.getBudgetByUserId()
        }
    }

}