package com.estebancoloradogonzalez.listify.model.database.service

import com.estebancoloradogonzalez.listify.model.database.AppDatabase
import com.estebancoloradogonzalez.listify.model.database.utils.ProductsSeed
import com.estebancoloradogonzalez.listify.model.database.utils.ProductData
import com.estebancoloradogonzalez.listify.model.entity.*
import java.time.LocalDateTime

class ProductRegistrationService(private val db: AppDatabase) {

    suspend fun registerTestProducts(userId: Long) {
        ProductsSeed.products.forEach { product ->
            registerProduct(
                product.name,
                product.price,
                product.quantity,
                product.unit,
                product.frequency,
                product.store,
                product.category,
                userId,
                db
            )
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
                                db: AppDatabase) {
        val amountDAO = db.amountDao()
        val amountUnitOfMeasurementDAO = db.amountUnitOfMeasurementDao()
        val categoryDAO = db.categoryDao()
        val establishmentDAO = db.establishmentDao()
        val productCategoryDAO = db.productCategoryDao()
        val productDAO = db.productDao()
        val productEstablishmentDAO = db.productEstablishmentDao()
        val productPurchaseFrenquencyDAO = db.productPurchaseFrenquencyDao()
        val purchaseFrenquencyDAO = db.purchaseFrequencyDao()
        val unitOfMeasurementDAO = db.unitOfMeasurementDao()

        val amount = Amount(value = productQuantity.toDouble())
        val amountId = amountDAO.insert(amount)

        val unitOfMeasurement = unitOfMeasurementDAO.getUnitOfMeasurementByName(selectedUnitOfMeasurement)

        if(unitOfMeasurement != null) {
            val amountUnitOfMeasurement = AmountUnitOfMeasurement(amount = amountId, unitOfMeasurement = unitOfMeasurement.id)
            amountUnitOfMeasurementDAO.insert(amountUnitOfMeasurement)
        }

        val product = Product(name = productName, unitPrice = productPrice.toDouble(), isActive = isNotActiveProduct(productName), amount = amountId, user = userId, registrationDate = LocalDateTime.now())
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
    }

    private fun isNotActiveProduct(productName: String): Boolean {
        return productName != ProductData.Nescafe.NAME &&
                productName != ProductData.Ramen.NAME &&
                productName != ProductData.SolomitoDeCerdo.NAME &&
                productName != ProductData.PapasCriollas.NAME
    }
}