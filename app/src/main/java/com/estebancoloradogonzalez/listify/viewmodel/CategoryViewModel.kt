package com.estebancoloradogonzalez.listify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.estebancoloradogonzalez.listify.model.database.AppDatabase
import com.estebancoloradogonzalez.listify.model.entity.Category
import com.estebancoloradogonzalez.listify.utils.InputValidator
import com.estebancoloradogonzalez.listify.utils.Messages
import kotlinx.coroutines.launch

class CategoryViewModel(application: Application) : AndroidViewModel(application) {

    private val categoryDAO = AppDatabase.getDatabase(application).categoryDao()

    fun createCategory(name: String, onError: (String) -> Unit, onSuccess: () -> Unit) {
        if (!InputValidator.isValidName(name)) {
            onError(Messages.ENTER_VALID_CATEGORY_NAME_MESSAGE)
            return
        }

        if (name.length > 100) {
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
}
