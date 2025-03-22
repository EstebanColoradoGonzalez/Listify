package com.estebancoloradogonzalez.listify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.estebancoloradogonzalez.listify.model.database.AppDatabase
import com.estebancoloradogonzalez.listify.model.entity.Budget
import com.estebancoloradogonzalez.listify.utils.InputValidator
import com.estebancoloradogonzalez.listify.utils.Messages
import kotlinx.coroutines.launch

class BudgetViewModel(application: Application) : AndroidViewModel(application) {
    private val budgetDao = AppDatabase.getDatabase(application).budgetDao()

    fun insertBudget(value: String, userId: Long, onError: (String) -> Unit) {
        if (!InputValidator.isValidBudget(value)) {
            onError(Messages.ENTER_VALID_BUDGET_MESSAGE)
            return
        }

        viewModelScope.launch {
            val newBudget = Budget(value = value.toDouble(), userId = userId)
            budgetDao.insertBudget(newBudget)
        }
    }
}