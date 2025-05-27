package com.estebancoloradogonzalez.listify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.estebancoloradogonzalez.listify.model.database.AppDatabase
import com.estebancoloradogonzalez.listify.model.dto.EstablishmentNameDTO
import com.estebancoloradogonzalez.listify.model.dto.ProductShoppingListWithEstablishmentDTO
import com.estebancoloradogonzalez.listify.model.dto.ProductToAnalyzeDTO
import com.estebancoloradogonzalez.listify.model.dto.ShoppingListDTO
import com.estebancoloradogonzalez.listify.model.dto.ShoppingListToAnalyzeDTO
import com.estebancoloradogonzalez.listify.model.dto.ShoppingListTotalDTO
import com.estebancoloradogonzalez.listify.model.entity.ProductShoppingList
import com.estebancoloradogonzalez.listify.model.entity.ShoppingList
import com.estebancoloradogonzalez.listify.model.entity.ShoppingListState
import com.estebancoloradogonzalez.listify.utils.Messages
import com.estebancoloradogonzalez.listify.utils.NumericConstants
import com.estebancoloradogonzalez.listify.utils.TextConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class ShoppingListViewModel(application: Application) : AndroidViewModel(application) {
    private val shoppingListDao = AppDatabase.getDatabase(application).shoppingListDao()
    private val productShoppingListDao = AppDatabase.getDatabase(application).productShoppingListDao()
    private val shoppingListStateDao = AppDatabase.getDatabase(application).shoppingListStateDao()
    private val productDao = AppDatabase.getDatabase(application).productDao()
    private val amountDao = AppDatabase.getDatabase(application).amountDao()
    private val stateDao = AppDatabase.getDatabase(application).stateDao()

    suspend fun fetchShoppingLists(userId: Long): List<ShoppingListDTO> = withContext(Dispatchers.IO) {
        shoppingListDao.getAllByUser(userId)
    }

    suspend fun fetchShoppingListById(shoppingListId: Long): ShoppingListDTO? = withContext(Dispatchers.IO) {
        shoppingListDao.getDTOById(shoppingListId)
    }

    suspend fun fetchTotalAmountByShoppingListAndEstablishment(
        shoppingListId: Long,
        establishmentName: String
    ): Double? = withContext(Dispatchers.IO) {
        productShoppingListDao.getTotalAmountByShoppingListAndEstablishment(shoppingListId, establishmentName)
    }

    suspend fun fetchEstablishmentsByShoppingList(shoppingListId: Long): List<EstablishmentNameDTO> = withContext(Dispatchers.IO) {
        shoppingListDao.getEstablishmentsByShoppingListId(shoppingListId)
    }

    suspend fun fetchProductsByShoppingListAndEstablishment(
        shoppingListId: Long,
        establishmentName: String
    ): List<ProductShoppingListWithEstablishmentDTO> = withContext(Dispatchers.IO) {
        shoppingListDao.getProductsByShoppingListAndEstablishment(shoppingListId, establishmentName)
    }

    suspend fun fetchShoppingListDateAndTotalAmount(shoppingListId: Long): ShoppingListTotalDTO? = withContext(Dispatchers.IO) {
        shoppingListDao.getDateAndTotalAmountByShoppingListId(shoppingListId)
    }

    fun updateProductIsReady(productShoppingListId: Long, isReady: Boolean) {
        viewModelScope.launch {
            productShoppingListDao.updateIsReadyById(productShoppingListId, isReady)
        }
    }

    suspend fun completeOrCancelShoppingList(
        userId: Long,
        shoppingListId: Long,
        stateName: String,
        onError: (String) -> Unit,
        onSuccess: () -> Unit
    ) {
        if (stateName == TextConstants.STATUS_COMPLETED && !areAllProductsReady(shoppingListId, onError)) return

        viewModelScope.launch {
            val state = stateDao.getByName(stateName)
            val shoppingListState = shoppingListStateDao.getByShoppingList(shoppingListId)

            if (state != null && shoppingListState != null) {
                shoppingListStateDao.updateStateById(shoppingListState.id, state.id)
            }

            if (stateName == TextConstants.STATUS_COMPLETED) {
                updateProductsOnListCompletion(userId, shoppingListId)
            }

            onSuccess()
        }
    }

    suspend fun generateShoppingList(
        date: LocalDateTime,
        userId: Long,
        onError: (String) -> Unit,
        onSuccess: () -> Unit
    ) {
        val products = productDao.getProductsToAnalyzeDTO(userId)
        val lastShoppingList = shoppingListDao.getLastByUser(userId)
        val allShoppingLists = fetchShoppingListsToAnalyze(userId)

        if (products.isEmpty()) {
            onError(Messages.THERE_ARE_NOT_ACTIVE_PRODUCTS)
            return
        }

        if (!isValidShoppingListDate(date, lastShoppingList, onError)) return

        viewModelScope.launch {
            val shoppingListId = createShoppingList(date, userId)
            createShoppingListState(shoppingListId)

            if (allShoppingLists.isEmpty()) {
                addAllProductsToShoppingList(products, shoppingListId)
            } else {
                addProductsBasedOnFrequency(products, allShoppingLists, date, lastShoppingList, shoppingListId)
            }

            onSuccess()
        }
    }

    suspend fun fetchShoppingListsToAnalyze(userId: Long): List<ShoppingListToAnalyzeDTO> = withContext(Dispatchers.IO) {
        val shoppingLists = shoppingListDao.getToAnalyzeByUser(userId)
        shoppingLists.map { shoppingListDTO ->
            val products = shoppingListDao.getProductsToAnalyzeDTO(shoppingListDTO.id)
            ShoppingListToAnalyzeDTO(
                id = shoppingListDTO.id,
                date = shoppingListDTO.date,
                status = shoppingListDTO.status,
                products = products
            )
        }
    }

    fun findShoppingListByFrequency(
        allShoppingLists: List<ShoppingListToAnalyzeDTO>,
        date: LocalDateTime,
        purchaseFrequency: String
    ): ShoppingListToAnalyzeDTO? {
        val thresholdDate = getDateThreshold(date, purchaseFrequency)
        return allShoppingLists.firstOrNull { it.date.isBefore(thresholdDate) }
    }

    fun getDateThreshold(date: LocalDateTime, purchaseFrequency: String): LocalDateTime = when (purchaseFrequency) {
        TextConstants.FREQUENCY_WEEKLY -> date.minusWeeks(NumericConstants.LONG_ONE)
        TextConstants.FREQUENCY_FORTNIGHTLY -> date.minusWeeks(NumericConstants.LONG_TWO)
        TextConstants.FREQUENCY_MONTHLY -> date.minusWeeks(NumericConstants.LONG_FOUR)
        TextConstants.FREQUENCY_BIMONTHLY -> date.minusWeeks(NumericConstants.LONG_EIGHT)
        TextConstants.FREQUENCY_QUARTERLY -> date.minusWeeks(NumericConstants.LONG_TWELVE)
        TextConstants.FREQUENCY_FOUR_MONTHLY -> date.minusWeeks(NumericConstants.LONG_SIXTEEN)
        TextConstants.FREQUENCY_SEMIANNUAL -> date.minusWeeks(NumericConstants.LONG_TWENTY_FOUR)
        else -> throw IllegalArgumentException(Messages.NOT_SUPPORTED_FREQUENCY + purchaseFrequency)
    }

    private suspend fun areAllProductsReady(
        shoppingListId: Long,
        onError: (String) -> Unit
    ): Boolean {
        val products = productShoppingListDao.getByShoppingList(shoppingListId)
        val allReady = products.all { it.isReady }
        if (!allReady) {
            onError(Messages.THERE_ARE_PRODUCTS_HAVE_NOT_BEEN_COMPLETED)
        }
        return allReady
    }

    private suspend fun updateProductsOnListCompletion(userId: Long, shoppingListId: Long) {
        val products = productDao.getActiveProductsWithAmountByUser(userId)
        val productsToAnalyze = shoppingListDao.getProductsToAnalyzeDTO(shoppingListId)
        products.forEach { product ->
            val productToAnalyze = productsToAnalyze.find { it.name == product.productName }
            if (productToAnalyze != null && (productToAnalyze.unitPrice != product.unitPrice || productToAnalyze.amount != product.amountValue)) {
                productDao.updateUnitPrice(product.productId, productToAnalyze.unitPrice)
                amountDao.updateValueById(product.amountId, productToAnalyze.amount)
            }
        }
    }

    private fun isValidShoppingListDate(
        date: LocalDateTime,
        lastShoppingList: ShoppingListDTO?,
        onError: (String) -> Unit
    ): Boolean {
        if (lastShoppingList == null) return true
        val lastDate = lastShoppingList.date
        val weeksBetween = ChronoUnit.WEEKS.between(lastDate, date)
        when {
            date.isBefore(lastDate) -> {
                onError(Messages.DATE_IS_BEFORE_LAST_SHOPPING_LIST + date.format(DateTimeFormatter.ISO_DATE))
                return false
            }
            weeksBetween < NumericConstants.POSITIVE_ONE -> {
                onError(Messages.DATE_IS_NOT_SEVEN_DAYS_AFTER)
                return false
            }
            lastShoppingList.status == TextConstants.STATUS_ACTIVE -> {
                onError(Messages.THERE_IS_AN_ACTIVE_SHOPPING_LIST)
                return false
            }
        }
        return true
    }

    private suspend fun createShoppingList(date: LocalDateTime, userId: Long): Long {
        val shoppingList = ShoppingList(shoppingListDate = date, user = userId)
        return shoppingListDao.insert(shoppingList)
    }

    private suspend fun createShoppingListState(shoppingListId: Long) {
        val state = stateDao.getByName(TextConstants.STATUS_ACTIVE) ?: return
        val shoppingListState = ShoppingListState(shoppingList = shoppingListId, state = state.id)
        shoppingListStateDao.insert(shoppingListState)
    }

    private suspend fun addAllProductsToShoppingList(products: List<ProductToAnalyzeDTO>, shoppingListId: Long) {
        products.forEach { product ->
            val productShoppingList = ProductShoppingList(
                unitPrice = product.unitPrice,
                purchasedAmount = product.amount,
                isReady = false,
                shoppingList = shoppingListId,
                product = product.id
            )
            productShoppingListDao.insert(productShoppingList)
        }
    }

    private suspend fun addProductsBasedOnFrequency(
        products: List<ProductToAnalyzeDTO>,
        allShoppingLists: List<ShoppingListToAnalyzeDTO>,
        date: LocalDateTime,
        lastShoppingList: ShoppingListDTO?,
        shoppingListId: Long
    ) {
        products.forEach { product ->
            val previousList = findShoppingListByFrequency(allShoppingLists, date, product.purchaseFrequency)
            if (previousList != null) {
                val matchedProduct = previousList.products.firstOrNull { it.name == product.name }
                if (matchedProduct != null) {
                    insertProductShoppingList(product, shoppingListId)
                }
            } else if (lastShoppingList != null && product.registrationDate.isAfter(lastShoppingList.date)) {
                insertProductShoppingList(product, shoppingListId)
            }
        }
    }

    private suspend fun insertProductShoppingList(
        product: ProductToAnalyzeDTO,
        shoppingListId: Long
    ) {
        val productShoppingList = ProductShoppingList(
            unitPrice = product.unitPrice,
            purchasedAmount = product.amount,
            isReady = false,
            shoppingList = shoppingListId,
            product = product.id
        )
        productShoppingListDao.insert(productShoppingList)
    }
}
