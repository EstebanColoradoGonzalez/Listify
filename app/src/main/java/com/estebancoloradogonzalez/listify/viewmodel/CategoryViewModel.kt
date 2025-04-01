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

    private val categoryDAO = AppDatabase.getDatabase(application).categoryDao()

    suspend fun getCategories(): List<Category> {
        return withContext(Dispatchers.IO) {
            categoryDAO.getCategories()
        }
    }

    fun createCategory(name: String, onError: (String) -> Unit, onSuccess: () -> Unit) {
        if (!InputValidator.isValidName(name)) {
            onError(Messages.ENTER_VALID_CATEGORY_NAME_MESSAGE)
            return
        }

        if (name.length > NumericConstants.POSITIVE_ONE_HUNDRED) {
            onError(Messages.CATEGORY_NAME_TOO_LONG_MESSAGE)
            return
        }

        viewModelScope.launch {
            val existingCategory = categoryDAO.getCategoryByName(name)
            if (existingCategory != null) {
                onError(Messages.CATEGORY_NAME_ALREADY_EXISTS_MESSAGE)
            } else {
                val newCategory = Category(name = name)
                categoryDAO.insert(newCategory)
                onSuccess()
            }
        }
    }

    fun updateCategory(id: Long, newName: String, onError: (String) -> Unit, onSuccess: () -> Unit) {
        if (!InputValidator.isValidName(newName)) {
            onError(Messages.ENTER_VALID_CATEGORY_NAME_MESSAGE)
            return
        }

        if (newName.length > NumericConstants.POSITIVE_ONE_HUNDRED) {
            onError(Messages.CATEGORY_NAME_TOO_LONG_MESSAGE)
            return
        }

        viewModelScope.launch {
            val existingCategory = categoryDAO.getCategoryByName(newName)
            if (existingCategory != null) {
                onError(Messages.CATEGORY_NAME_ALREADY_EXISTS_MESSAGE)
            } else {
                categoryDAO.updateCategory(id, newName)
                onSuccess()
            }
        }
    }

    fun deleteCategory(id: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            categoryDAO.deleteCategoryById(id)
            onSuccess()
        }
    }

    suspend fun getCategoryById(id: Long): Category? {
        return withContext(Dispatchers.IO) {
            categoryDAO.getCategoryById(id)
        }
    }
}
