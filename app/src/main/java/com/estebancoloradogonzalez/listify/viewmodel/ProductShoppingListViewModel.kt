package com.estebancoloradogonzalez.listify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.estebancoloradogonzalez.listify.model.database.AppDatabase
import com.estebancoloradogonzalez.listify.model.dto.ProductShoppingListDetailDTO
import com.estebancoloradogonzalez.listify.utils.InputValidator
import com.estebancoloradogonzalez.listify.utils.Messages
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductShoppingListViewModel(application: Application) : AndroidViewModel(application)  {
    private val productShoppingListDAO = AppDatabase.getDatabase(application).productShoppingListDao()

    suspend fun get(productShoppingListId: Long): ProductShoppingListDetailDTO? {
        return withContext(Dispatchers.IO) {
            productShoppingListDAO.getProductShoppingListDetailById(productShoppingListId)
        }
    }

    fun updateProduct(id: Long, unitPrice: String, purchasedAmount: String,
                              onError: (String) -> Unit, onSuccess: () -> Unit
    ) {
        if (!InputValidator.isValidNumericValue(unitPrice)) {
            onError(Messages.ENTER_VALID_PRICE_MESSAGE)
            return
        }

        if (!InputValidator.isValidNumericValue(purchasedAmount)) {
            onError(Messages.ENTER_VALID_QUANTITY_MESSAGE)
            return
        }

        viewModelScope.launch {
            productShoppingListDAO.updateById(id = id, unitPrice = unitPrice.toDouble(), purchasedAmount = purchasedAmount.toDouble())

            onSuccess()
        }
    }

    fun deleteProduct(id: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            productShoppingListDAO.deleteById(id = id)

            onSuccess()
        }
    }
}