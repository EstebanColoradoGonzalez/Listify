package com.estebancoloradogonzalez.listify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.estebancoloradogonzalez.listify.model.database.AppDatabase
import com.estebancoloradogonzalez.listify.model.dto.ProductDTO
import com.estebancoloradogonzalez.listify.model.entity.Amount
import com.estebancoloradogonzalez.listify.model.entity.AmountUnitOfMeasurement
import com.estebancoloradogonzalez.listify.model.entity.Product
import com.estebancoloradogonzalez.listify.model.entity.ProductCategory
import com.estebancoloradogonzalez.listify.model.entity.ProductEstablishment
import com.estebancoloradogonzalez.listify.model.entity.ProductPurchaseFrequency
import com.estebancoloradogonzalez.listify.utils.InputValidator
import com.estebancoloradogonzalez.listify.utils.Messages
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductViewModel(application: Application) : AndroidViewModel(application) {
    private val amountDAO = AppDatabase.getDatabase(application).amountDao()
    private val amountUnitOfMeasurementDAO = AppDatabase.getDatabase(application).amountUnitOfMeasurementDao()
    private val categoryDAO = AppDatabase.getDatabase(application).categoryDao()
    private val establishmentDAO = AppDatabase.getDatabase(application).establishmentDao()
    private val productCategoryDAO = AppDatabase.getDatabase(application).productCategoryDao()
    private val productDAO = AppDatabase.getDatabase(application).productDao()
    private val productEstablishmentDAO = AppDatabase.getDatabase(application).productEstablishmentDao()
    private val productPurchaseFrenquencyDAO = AppDatabase.getDatabase(application).productPurchaseFrenquencyDao()
    private val purchaseFrenquencyDAO = AppDatabase.getDatabase(application).purchaseFrequencyDao()
    private val unitOfMeasurementDAO = AppDatabase.getDatabase(application).unitOfMeasurementDao()

    suspend fun getProducts(user: Long): List<ProductDTO> {
        return withContext(Dispatchers.IO) {
            productDAO.getProducts(user)
        }
    }

    fun registerProduct(productName: String,
                     productPrice: String,
                     productQuantity: String,
                     selectedUnitOfMeasurement: String,
                     selectedPurchaseFrequency: String,
                     selectedEstablishment: String,
                     selectedCategory: String,
                     userId: Long,
                     onError: (String) -> Unit, onSuccess: () -> Unit) {
        if (!InputValidator.isValidName(productName)) {
            onError(Messages.ENTER_VALID_NAME_MESSAGE)
            return
        }

        if (!InputValidator.isValidNumericValue(productPrice)) {
            onError(Messages.ENTER_VALID_PRICE_MESSAGE)
            return
        }

        if (!InputValidator.isValidNumericValue(productQuantity)) {
            onError(Messages.ENTER_VALID_QUANTITY_MESSAGE)
            return
        }

        viewModelScope.launch {
            val amount = Amount(value = productQuantity.toDouble())
            val amountId = amountDAO.insert(amount)

            val unitOfMeasurement = unitOfMeasurementDAO.getUnitOfMeasurementByName(selectedUnitOfMeasurement)

            if(unitOfMeasurement != null) {
                val amountUnitOfMeasurement = AmountUnitOfMeasurement(amount = amountId, unitOfMeasurement = unitOfMeasurement.id)
                amountUnitOfMeasurementDAO.insert(amountUnitOfMeasurement)
            }

            val product = Product(name = productName, unitPrice = productPrice.toDouble(), isActive = true, amount = amountId, user = userId)
            val productId = productDAO.insert(product)

            val establishment = establishmentDAO.getEstablishmentByName(selectedEstablishment)

            if(establishment != null) {
                val productEstablishment = ProductEstablishment(product = productId, establishment = establishment.id)
                productEstablishmentDAO.insert(productEstablishment)
            }

            val purchaseFrequency = purchaseFrenquencyDAO.getPurchaseFrequencyByName(selectedPurchaseFrequency)

            if(purchaseFrequency != null) {
                val productPurchaseFrequency = ProductPurchaseFrequency(product = productId, purchaseFrequency = purchaseFrequency.id)
                productPurchaseFrenquencyDAO.insert(productPurchaseFrequency)
            }

            val category = categoryDAO.getCategoryByName(selectedCategory)

            if(category != null) {
                val productCategory = ProductCategory(product = productId, category = category.id)
                productCategoryDAO.insert(productCategory)
            }

            onSuccess()
        }
    }
}