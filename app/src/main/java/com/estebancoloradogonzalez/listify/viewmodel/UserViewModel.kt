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
        checkUser()
    }

    private fun checkUser() {
        viewModelScope.launch {
            _user.postValue(userDao.getUser())
        }
    }

    fun registerUser(name: String, budget: String, onError: (String) -> Unit, onUserRegistered: (Long) -> Unit) {
        if (!InputValidator.isValidBudget(budget)) {
            onError(Messages.ENTER_VALID_BUDGET_MESSAGE)
            return
        }

        viewModelScope.launch {
            val newBudget = Budget(value = budget.toDouble())
            budgetDao.insertBudget(newBudget)

            val budgetId = budgetDao.getBudgetId()

            val newUser = User(name = name, registrationDate = LocalDateTime.now(), budget = budgetId)
            userDao.insertUser(newUser)

            val userId = userDao.getUserId()
            _user.postValue(newUser)

            onUserRegistered(userId)
        }
    }

    suspend fun getUserId(): Long {
        return withContext(Dispatchers.IO) {
            userDao.getUserId()
        }
    }


    fun updateUserName(userId: Long, newName: String, onError: (String) -> Unit) {
        if (!InputValidator.isValidName(newName)) {
            onError(Messages.ENTER_VALID_NAME_MESSAGE)
            return
        }
        viewModelScope.launch {
            userDao.updateUserName(userId, newName)
            checkUser()
        }
    }

}