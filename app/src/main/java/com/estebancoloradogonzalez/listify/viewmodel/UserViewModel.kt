package com.estebancoloradogonzalez.listify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.estebancoloradogonzalez.listify.model.database.AppDatabase
import com.estebancoloradogonzalez.listify.model.entity.Budget
import com.estebancoloradogonzalez.listify.model.entity.User
import com.estebancoloradogonzalez.listify.utils.InputValidator
import com.estebancoloradogonzalez.listify.utils.Messages
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val budgetDao = AppDatabase.getDatabase(application).budgetDao()
    private val userDao = AppDatabase.getDatabase(application).userDao()

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            _user.postValue(userDao.get())
        }
    }

    fun registerUser(
        name: String,
        budget: String,
        onError: (String) -> Unit,
        onUserRegistered: (Long) -> Unit
    ) {
        if (!isBudgetValid(budget, onError)) return

        viewModelScope.launch {
            val budgetId = insertBudgetAndGetId(budget)
            val userId = insertUserAndGetId(name, budgetId)
            _user.postValue(User(name = name, registrationDate = LocalDateTime.now(), budget = budgetId))
            onUserRegistered(userId)
        }
    }

    suspend fun fetchUserId(): Long = withContext(Dispatchers.IO) {
        userDao.getId()
    }

    fun updateUserName(userId: Long, newName: String, onError: (String) -> Unit) {
        if (!InputValidator.isValidName(newName)) {
            onError(Messages.ENTER_VALID_NAME_MESSAGE)
            return
        }
        viewModelScope.launch {
            userDao.updateNameById(userId, newName)
            loadUser()
        }
    }

    private fun isBudgetValid(budget: String, onError: (String) -> Unit): Boolean {
        if (!InputValidator.isValidNumericValue(budget)) {
            onError(Messages.ENTER_VALID_BUDGET_MESSAGE)
            return false
        }
        return true
    }

    private suspend fun insertBudgetAndGetId(budget: String): Long {
        val newBudget = Budget(value = budget.toDouble())
        budgetDao.insert(newBudget)
        return budgetDao.getId()
    }

    private suspend fun insertUserAndGetId(name: String, budgetId: Long): Long {
        val newUser = User(name = name, registrationDate = LocalDateTime.now(), budget = budgetId)
        userDao.insert(newUser)
        return userDao.getId()
    }
}