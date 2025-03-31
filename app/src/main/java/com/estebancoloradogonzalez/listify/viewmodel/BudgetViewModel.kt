package com.estebancoloradogonzalez.listify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.estebancoloradogonzalez.listify.model.database.AppDatabase
import com.estebancoloradogonzalez.listify.model.entity.Budget
import com.estebancoloradogonzalez.listify.utils.InputValidator
import com.estebancoloradogonzalez.listify.utils.Messages
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BudgetViewModel(application: Application) : AndroidViewModel(application) {
    private val budgetDao = AppDatabase.getDatabase(application).budgetDao()

    fun updateBudget(id: Long, newBudget: String, onError: (String) -> Unit) {
        if (!InputValidator.isValidBudget(newBudget)) {
            onError(Messages.ENTER_VALID_BUDGET_MESSAGE)
            return
        }

        viewModelScope.launch { budgetDao.updateBudget(id, newBudget.toDouble()) }
    }

    suspend fun getUserBudget(): Budget? {
        return withContext(Dispatchers.IO) {
            budgetDao.getBudget()
        }
    }
}