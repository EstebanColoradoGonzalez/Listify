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

    suspend fun getTotalExpenditure(): Double? {
        return withContext(Dispatchers.IO) {
            productDAO.getTotalExpenditure()
        }
    }

    suspend fun getProductToUpdate(productId: Long): ProductToUpdateDTO? {
        return withContext(Dispatchers.IO) {
            productDAO.getProductToUpdate(productId)
        }
    }

    suspend fun getProductById(productId: Long): Product? {
        return withContext(Dispatchers.IO) {
            productDAO.getById(productId)
        }
    }

    suspend fun registerProduct(productName: String,
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

        if (productDAO.getByName(productName) != null) {
            onError(Messages.PRODUCT_ALREADY_EXISTS)
            return
        }

        if (productDAO.isTotalExpenditureExceedBudgetToCreate(userId, productPrice.toDouble() * productQuantity.toDouble())) {
            onError(Messages.BUDGET_EXCEEDED)
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

    suspend fun updateProduct(
        productName: String,
        productPrice: String,
        productQuantity: String,
        selectedUnitOfMeasurement: String,
        selectedPurchaseFrequency: String,
        selectedEstablishment: String,
        selectedCategory: String,
        active: Boolean,
        productId: Long,
        userId: Long,
        onError: (String) -> Unit, onSuccess: () -> Unit
    ) {
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

        if (productDAO.getByNameToUpdate(productName, productId) != null) {
            onError(Messages.PRODUCT_ALREADY_EXISTS)
            return
        }

        if (productDAO.isTotalExpenditureExceedBudgetToUpdate(userId, productPrice.toDouble() * productQuantity.toDouble(), productId)) {
            onError(Messages.BUDGET_EXCEEDED)
            return
        }

        viewModelScope.launch {
            productDAO.updateProductById(productId, productName, productPrice.toDouble(), active)

            val product = productDAO.getById(productId)

            if(product != null) {
                amountDAO.updateValueById(product.amount, productQuantity.toDouble())

                val amountUnitOfMeasurement = amountUnitOfMeasurementDAO.getByAmount(product.amount)
                val unitOfMeasurement = unitOfMeasurementDAO.getUnitOfMeasurementByName(selectedUnitOfMeasurement)

                if(amountUnitOfMeasurement != null && unitOfMeasurement != null) {
                    amountUnitOfMeasurementDAO.updateUnitById(amountUnitOfMeasurement.id, unitOfMeasurement.id)
                }

                val productEstablishment = productEstablishmentDAO.getByProduct(productId)
                val establishment = establishmentDAO.getEstablishmentByName(selectedEstablishment)

                if(productEstablishment != null && establishment != null) {
                    productEstablishmentDAO.updateEstablishmentById(productEstablishment.id, establishment.id)
                }

                val productPurchaseFrequency = productPurchaseFrenquencyDAO.getByProduct(productId)
                val purchaseFrequency = purchaseFrenquencyDAO.getPurchaseFrequencyByName(selectedPurchaseFrequency)

                if(productPurchaseFrequency != null && purchaseFrequency != null) {
                    productPurchaseFrenquencyDAO.updatePurchaseFrequencyById(productPurchaseFrequency.id, purchaseFrequency.id)
                }

                val productCategory = productCategoryDAO.getByProduct(productId)
                val category = categoryDAO.getCategoryByName(selectedCategory)

                if(productCategory != null && category != null) {
                    productCategoryDAO.updateCategoryById(productCategory.id, category.id)
                }

                onSuccess()
            }
        }
    }

    fun deleteProduct(id: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val product = productDAO.getById(id)


            if(product != null) {
                val productCategory = productCategoryDAO.getByProduct(id)

                if(productCategory != null) {
                    productCategoryDAO.deleteById(productCategory.id)
                }

                val productPurchaseFrequency = productPurchaseFrenquencyDAO.getByProduct(id)

                if(productPurchaseFrequency != null) {
                    productPurchaseFrenquencyDAO.deleteById(productPurchaseFrequency.id)
                }

                val productEstablishment = productEstablishmentDAO.getByProduct(id)

                if(productEstablishment != null) {
                    productEstablishmentDAO.deleteById(productEstablishment.id)
                }

                productDAO.deleteById(id)

                val amountUnitOfMeasurement = amountUnitOfMeasurementDAO.getByAmount(product.amount)

                if(amountUnitOfMeasurement != null) {
                    amountUnitOfMeasurementDAO.deleteById(amountUnitOfMeasurement.id)
                }

                amountDAO.deleteById(product.amount)

                onSuccess()
            }
        }
    }
}