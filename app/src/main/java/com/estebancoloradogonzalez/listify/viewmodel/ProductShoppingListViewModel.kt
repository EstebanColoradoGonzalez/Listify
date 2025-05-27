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

class ProductShoppingListViewModel(application: Application) : AndroidViewModel(application) {
    private val productShoppingListDao = AppDatabase.getDatabase(application).productShoppingListDao()
    private val productDao = AppDatabase.getDatabase(application).productDao()
    private val amountDao = AppDatabase.getDatabase(application).amountDao()
    private val shoppingListDao = AppDatabase.getDatabase(application).shoppingListDao()

    suspend fun fetchProductShoppingListDetail(productShoppingListId: Long): ProductShoppingListDetailDTO? =
        withContext(Dispatchers.IO) {
            productShoppingListDao.getDetailById(productShoppingListId)
        }

    suspend fun fetchAvailableProductsForSelection(
        shoppingListId: Long,
        establishmentName: String
    ): List<ProductIdNameDTO> = withContext(Dispatchers.IO) {
        val allProducts = productDao.getProductsByEstablishmentName(establishmentName)
        val selectedProducts = shoppingListDao.getProductsByShoppingListAndEstablishment(shoppingListId, establishmentName)
        val selectedProductNames = selectedProducts.map { it.productName }.toSet()
        allProducts.filter { it.name !in selectedProductNames }
    }

    fun addProductToShoppingList(
        productId: Long,
        shoppingListId: Long,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val product = productDao.getById(productId)
            val amount = product?.let { amountDao.getById(it.amount) }
            if (product != null && amount != null) {
                val productShoppingList = ProductShoppingList(
                    unitPrice = product.unitPrice,
                    purchasedAmount = amount.value,
                    isReady = false,
                    shoppingList = shoppingListId,
                    product = product.id
                )
                productShoppingListDao.insert(productShoppingList)
            }
            onSuccess()
        }
    }

    fun updateProductInShoppingList(
        id: Long,
        unitPrice: String,
        purchasedAmount: String,
        onError: (String) -> Unit,
        onSuccess: () -> Unit
    ) {
        if (!isValidUnitPrice(unitPrice, onError) || !isValidPurchasedAmount(purchasedAmount, onError)) return

        viewModelScope.launch {
            productShoppingListDao.updateById(
                id = id,
                unitPrice = unitPrice.toDouble(),
                purchasedAmount = purchasedAmount.toDouble()
            )
            onSuccess()
        }
    }

    fun removeProductFromShoppingList(id: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            productShoppingListDao.deleteById(id)
            onSuccess()
        }
    }

    private fun isValidUnitPrice(unitPrice: String, onError: (String) -> Unit): Boolean {
        if (!InputValidator.isValidNumericValue(unitPrice)) {
            onError(Messages.ENTER_VALID_PRICE_MESSAGE)
            return false
        }
        return true
    }

    private fun isValidPurchasedAmount(purchasedAmount: String, onError: (String) -> Unit): Boolean {
        if (!InputValidator.isValidNumericValue(purchasedAmount)) {
            onError(Messages.ENTER_VALID_QUANTITY_MESSAGE)
            return false
        }
        return true
    }
}