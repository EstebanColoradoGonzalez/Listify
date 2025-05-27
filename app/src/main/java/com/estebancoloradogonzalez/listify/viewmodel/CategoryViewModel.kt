package com.estebancoloradogonzalez.listify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.estebancoloradogonzalez.listify.model.database.AppDatabase
import com.estebancoloradogonzalez.listify.model.entity.Category
import com.estebancoloradogonzalez.listify.utils.InputValidator
import com.estebancoloradogonzalez.listify.utils.Messages
import com.estebancoloradogonzalez.listify.utils.NumericConstants
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoryViewModel(application: Application) : AndroidViewModel(application) {
    private val categoryDao = AppDatabase.getDatabase(application).categoryDao()

    suspend fun fetchCategories(): List<Category> = withContext(Dispatchers.IO) {
        categoryDao.getAll()
    }

    suspend fun fetchCategoryById(id: Long): Category? = withContext(Dispatchers.IO) {
        categoryDao.getById(id)
    }

    fun addCategory(name: String, onError: (String) -> Unit, onSuccess: () -> Unit) {
        if (!isCategoryNameValid(name, onError)) return

        viewModelScope.launch {
            if (isCategoryNameExists(name)) {
                onError(Messages.CATEGORY_NAME_ALREADY_EXISTS_MESSAGE)
            } else {
                categoryDao.insert(Category(name = name))
                onSuccess()
            }
        }
    }

    fun modifyCategory(id: Long, newName: String, onError: (String) -> Unit, onSuccess: () -> Unit) {
        if (!isCategoryNameValid(newName, onError)) return

        viewModelScope.launch {
            if (isCategoryNameExists(newName)) {
                onError(Messages.CATEGORY_NAME_ALREADY_EXISTS_MESSAGE)
            } else {
                categoryDao.updateNameById(id, newName)
                onSuccess()
            }
        }
    }

    fun removeCategory(id: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            categoryDao.deleteById(id)
            onSuccess()
        }
    }

    private fun isCategoryNameValid(name: String, onError: (String) -> Unit): Boolean {
        if (!InputValidator.isValidName(name)) {
            onError(Messages.ENTER_VALID_CATEGORY_NAME_MESSAGE)
            return false
        }

        if (name.length > NumericConstants.POSITIVE_ONE_HUNDRED) {
            onError(Messages.CATEGORY_NAME_TOO_LONG_MESSAGE)
            return false
        }

        return true
    }

    private suspend fun isCategoryNameExists(name: String): Boolean {
        return withContext(Dispatchers.IO) {
            categoryDao.getByName(name) != null
        }
    }
}