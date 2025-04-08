package com.estebancoloradogonzalez.listify.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.estebancoloradogonzalez.listify.model.database.AppDatabase
import com.estebancoloradogonzalez.listify.model.dto.ShoppingListDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ShoppingListViewModel(application: Application) : AndroidViewModel(application) {
    private val shoppingListDAO = AppDatabase.getDatabase(application).shoppingListDao()
    private val shoppingListStateDAO = AppDatabase.getDatabase(application).shoppingListStateDao()
    private val productShoppingListDAO = AppDatabase.getDatabase(application).productShoppingListDao()
    private val productDAO = AppDatabase.getDatabase(application).productDao()

    suspend fun getShoppingLists(user: Long): List<ShoppingListDTO> {
        return withContext(Dispatchers.IO) {
            shoppingListDAO.getShoppingLists(user)
        }
    }

    fun generateShoppingList(date: LocalDateTime, userId: Long, onError: (String) -> Unit, onSuccess: () -> Unit) {
        Log.d("Fecha: ", date.format(DateTimeFormatter.ISO_DATE))
        Log.d("User: ", userId.toString())
    }
}