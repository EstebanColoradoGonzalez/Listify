package com.estebancoloradogonzalez.listify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.estebancoloradogonzalez.listify.model.database.AppDatabase
import com.estebancoloradogonzalez.listify.model.dto.ProductDTO
import com.estebancoloradogonzalez.listify.model.dto.ProductToUpdateDTO
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
import java.time.LocalDateTime

class ProductViewModel(application: Application) : AndroidViewModel(application) {
    private val amountDao = AppDatabase.getDatabase(application).amountDao()
    private val amountUnitDao = AppDatabase.getDatabase(application).amountUnitOfMeasurementDao()
    private val categoryDao = AppDatabase.getDatabase(application).categoryDao()
    private val establishmentDao = AppDatabase.getDatabase(application).establishmentDao()
    private val productCategoryDao = AppDatabase.getDatabase(application).productCategoryDao()
    private val productDao = AppDatabase.getDatabase(application).productDao()
    private val productEstablishmentDao = AppDatabase.getDatabase(application).productEstablishmentDao()
    private val productPurchaseFrequencyDao = AppDatabase.getDatabase(application).productPurchaseFrenquencyDao()
    private val purchaseFrequencyDao = AppDatabase.getDatabase(application).purchaseFrequencyDao()
    private val unitOfMeasurementDao = AppDatabase.getDatabase(application).unitOfMeasurementDao()

    suspend fun fetchProducts(userId: Long): List<ProductDTO> = withContext(Dispatchers.IO) {
        productDao.getProducts(userId)
    }

    suspend fun fetchTotalExpenditure(): Double? = withContext(Dispatchers.IO) {
        productDao.getTotalExpenditure()
    }

    suspend fun fetchProductToUpdate(productId: Long): ProductToUpdateDTO? = withContext(Dispatchers.IO) {
        productDao.getProductToUpdate(productId)
    }

    suspend fun fetchProductById(productId: Long): Product? = withContext(Dispatchers.IO) {
        productDao.getById(productId)
    }

    suspend fun registerProduct(
        name: String,
        price: String,
        quantity: String,
        unitOfMeasurement: String,
        purchaseFrequency: String,
        establishment: String,
        category: String,
        userId: Long,
        onError: (String) -> Unit,
        onSuccess: () -> Unit
    ) {
        if (!isValidProductInput(name, price, quantity, onError)) return
        if (productDao.getByName(name) != null) {
            onError(Messages.PRODUCT_ALREADY_EXISTS)
            return
        }
        if (productDao.isTotalExpenditureExceedBudgetToCreate(userId, price.toDouble() * quantity.toDouble())) {
            onError(Messages.BUDGET_EXCEEDED)
            return
        }
        viewModelScope.launch {
            val amountId = amountDao.insert(Amount(value = quantity.toDouble()))
            linkAmountUnitOfMeasurement(amountId, unitOfMeasurement)
            val productId = productDao.insert(
                Product(
                    name = name,
                    unitPrice = price.toDouble(),
                    isActive = true,
                    amount = amountId,
                    user = userId,
                    registrationDate = LocalDateTime.now()
                )
            )
            linkProductEstablishment(productId, establishment)
            linkProductPurchaseFrequency(productId, purchaseFrequency)
            linkProductCategory(productId, category)
            onSuccess()
        }
    }

    suspend fun updateProduct(
        name: String,
        price: String,
        quantity: String,
        unitOfMeasurement: String,
        purchaseFrequency: String,
        establishment: String,
        category: String,
        active: Boolean,
        productId: Long,
        userId: Long,
        onError: (String) -> Unit,
        onSuccess: () -> Unit
    ) {
        if (!isValidProductInput(name, price, quantity, onError)) return
        if (productDao.getByNameToUpdate(name, productId) != null) {
            onError(Messages.PRODUCT_ALREADY_EXISTS)
            return
        }
        if (productDao.isTotalExpenditureExceedBudgetToUpdate(userId, price.toDouble() * quantity.toDouble(), productId)) {
            onError(Messages.BUDGET_EXCEEDED)
            return
        }
        viewModelScope.launch {
            productDao.updateProductById(productId, name, price.toDouble(), active)
            val product = productDao.getById(productId) ?: return@launch
            amountDao.updateValueById(product.amount, quantity.toDouble())
            updateAmountUnitOfMeasurement(product.amount, unitOfMeasurement)
            updateProductEstablishment(productId, establishment)
            updateProductPurchaseFrequency(productId, purchaseFrequency)
            updateProductCategory(productId, category)
            onSuccess()
        }
    }

    fun deleteProduct(productId: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val product = productDao.getById(productId) ?: return@launch
            deleteProductRelations(productId, product.amount)
            productDao.deleteById(productId)
            amountDao.deleteById(product.amount)
            onSuccess()
        }
    }

    private fun isValidProductInput(name: String, price: String, quantity: String, onError: (String) -> Unit): Boolean {
        if (!InputValidator.isValidName(name)) {
            onError(Messages.ENTER_VALID_NAME_MESSAGE)
            return false
        }
        if (!InputValidator.isValidNumericValue(price)) {
            onError(Messages.ENTER_VALID_PRICE_MESSAGE)
            return false
        }
        if (!InputValidator.isValidNumericValue(quantity)) {
            onError(Messages.ENTER_VALID_QUANTITY_MESSAGE)
            return false
        }
        return true
    }

    private suspend fun linkAmountUnitOfMeasurement(amountId: Long, unitOfMeasurement: String) {
        val unit = unitOfMeasurementDao.getByName(unitOfMeasurement) ?: return
        amountUnitDao.insert(AmountUnitOfMeasurement(amount = amountId, unitOfMeasurement = unit.id))
    }

    private suspend fun linkProductEstablishment(productId: Long, establishment: String) {
        val est = establishmentDao.getByName(establishment) ?: return
        productEstablishmentDao.insert(ProductEstablishment(product = productId, establishment = est.id))
    }

    private suspend fun linkProductPurchaseFrequency(productId: Long, purchaseFrequency: String) {
        val freq = purchaseFrequencyDao.getByName(purchaseFrequency) ?: return
        productPurchaseFrequencyDao.insert(ProductPurchaseFrequency(product = productId, purchaseFrequency = freq.id))
    }

    private suspend fun linkProductCategory(productId: Long, category: String) {
        val cat = categoryDao.getByName(category) ?: return
        productCategoryDao.insert(ProductCategory(product = productId, category = cat.id))
    }

    private suspend fun updateAmountUnitOfMeasurement(amountId: Long, unitOfMeasurement: String) {
        val amountUnit = amountUnitDao.getByAmount(amountId)
        val unit = unitOfMeasurementDao.getByName(unitOfMeasurement)
        if (amountUnit != null && unit != null) {
            amountUnitDao.updateUnitById(amountUnit.id, unit.id)
        }
    }

    private suspend fun updateProductEstablishment(productId: Long, establishment: String) {
        val productEst = productEstablishmentDao.getByProduct(productId)
        val est = establishmentDao.getByName(establishment)
        if (productEst != null && est != null) {
            productEstablishmentDao.updateEstablishmentById(productEst.id, est.id)
        }
    }

    private suspend fun updateProductPurchaseFrequency(productId: Long, purchaseFrequency: String) {
        val productFreq = productPurchaseFrequencyDao.getByProduct(productId)
        val freq = purchaseFrequencyDao.getByName(purchaseFrequency)
        if (productFreq != null && freq != null) {
            productPurchaseFrequencyDao.updatePurchaseFrequencyById(productFreq.id, freq.id)
        }
    }

    private suspend fun updateProductCategory(productId: Long, category: String) {
        val productCat = productCategoryDao.getByProduct(productId)
        val cat = categoryDao.getByName(category)
        if (productCat != null && cat != null) {
            productCategoryDao.updateCategoryById(productCat.id, cat.id)
        }
    }

    private suspend fun deleteProductRelations(productId: Long, amountId: Long) {
        productCategoryDao.getByProduct(productId)?.let { productCategoryDao.deleteById(it.id) }
        productPurchaseFrequencyDao.getByProduct(productId)?.let { productPurchaseFrequencyDao.deleteById(it.id) }
        productEstablishmentDao.getByProduct(productId)?.let { productEstablishmentDao.deleteById(it.id) }
        amountUnitDao.getByAmount(amountId)?.let { amountUnitDao.deleteById(it.id) }
    }
}