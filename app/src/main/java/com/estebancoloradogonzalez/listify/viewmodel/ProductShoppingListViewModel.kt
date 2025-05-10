package com.estebancoloradogonzalez.listify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.estebancoloradogonzalez.listify.model.database.AppDatabase
import com.estebancoloradogonzalez.listify.model.dto.ProductIdNameDTO
import com.estebancoloradogonzalez.listify.model.dto.ProductShoppingListDetailDTO
import com.estebancoloradogonzalez.listify.model.entity.ProductShoppingList
import com.estebancoloradogonzalez.listify.utils.InputValidator
import com.estebancoloradogonzalez.listify.utils.Messages
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductShoppingListViewModel(application: Application) : AndroidViewModel(application)  {
    private val productShoppingListDAO = AppDatabase.getDatabase(application).productShoppingListDao()
    private val productDAO = AppDatabase.getDatabase(application).productDao()
    private val amountDAO = AppDatabase.getDatabase(application).amountDao()
    private val shoppingListDAO = AppDatabase.getDatabase(application).shoppingListDao()

    suspend fun getProductShoppingListDetailById(productShoppingListId: Long): ProductShoppingListDetailDTO? {
        return withContext(Dispatchers.IO) {
            productShoppingListDAO.getProductShoppingListDetailById(productShoppingListId)
        }
    }

    suspend fun getProductsToSelect(
        shoppingListId: Long,
        establishmentName: String
    ): List<ProductIdNameDTO> {
        val allProducts = productDAO.getProductsByEstablishmentName(establishmentName)
        val selectedProducts = shoppingListDAO.getProductsByShoppingListAndEstablishment(shoppingListId, establishmentName)
        val selectedProductNames = selectedProducts.map { it.productName }.toSet()

        return withContext(Dispatchers.IO) {
            allProducts.filter { it.name !in selectedProductNames }
        }
    }

    fun selectProduct(productId: Long,
                      shoppingListId: Long,
                      onSuccess: () -> Unit) {
        viewModelScope.launch {
            val product = productDAO.getById(productId)

            if(product != null) {
                val amount = amountDAO.getAmountById(product.amount)

                if(amount != null) {
                    val productShoppingList = ProductShoppingList(unitPrice = product.unitPrice, purchasedAmount = amount.value, isReady = false, shoppingList = shoppingListId, product = product.id)

                    productShoppingListDAO.insert(productShoppingList)
                }
            }

            onSuccess()
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