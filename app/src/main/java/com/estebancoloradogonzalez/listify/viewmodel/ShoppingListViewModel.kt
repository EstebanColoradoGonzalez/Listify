package com.estebancoloradogonzalez.listify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.estebancoloradogonzalez.listify.model.database.AppDatabase
import java.time.LocalDateTime

class ShoppingListViewModel(application: Application) : AndroidViewModel(application) {
    private val shoppingListDAO = AppDatabase.getDatabase(application).shoppingListDao()
    private val shoppingListStateDAO = AppDatabase.getDatabase(application).shoppingListStateDao()
    private val productShoppingListDAO = AppDatabase.getDatabase(application).productShoppingListDao()
    private val productDAO = AppDatabase.getDatabase(application).productDao()

    fun generateShoppingList(date: LocalDateTime, onError: (String) -> Unit, onSuccess: () -> Unit) {

    }
}