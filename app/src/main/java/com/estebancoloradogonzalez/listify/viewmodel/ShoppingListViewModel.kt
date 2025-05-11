package com.estebancoloradogonzalez.listify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.estebancoloradogonzalez.listify.model.database.AppDatabase
import com.estebancoloradogonzalez.listify.model.dto.EstablishmentNameDTO
import com.estebancoloradogonzalez.listify.model.dto.ProductShoppingListWithEstablishmentDTO
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
    private val shoppingListDAO = AppDatabase.getDatabase(application).shoppingListDao()
    private val productShoppingListDAO = AppDatabase.getDatabase(application).productShoppingListDao()
    private val shoppingListStateDAO = AppDatabase.getDatabase(application).shoppingListStateDao()
    private val productDAO = AppDatabase.getDatabase(application).productDao()
    private val amountDAO = AppDatabase.getDatabase(application).amountDao()
    private val stateDAO = AppDatabase.getDatabase(application).stateDao()

    suspend fun getShoppingLists(user: Long): List<ShoppingListDTO> {
        return withContext(Dispatchers.IO) {
            shoppingListDAO.getShoppingLists(user)
        }
    }

    suspend fun getTotalAmountByShoppingListAndEstablishment(
        shoppingListId: Long,
        establishmentName: String
    ): Double? {
        return withContext(Dispatchers.IO) {
            productShoppingListDAO.getTotalAmountByShoppingListAndEstablishment(shoppingListId, establishmentName)
        }
    }

    suspend fun getEstablishmentFromAShoppingList(shoppingList: Long): List<EstablishmentNameDTO> {
        return withContext(Dispatchers.IO) {
            shoppingListDAO.getEstablishmentsForShoppingList(shoppingList)
        }
    }

    suspend fun getProductsByShoppingListAndEstablishment(shoppingListId: Long, establishmentName: String): List<ProductShoppingListWithEstablishmentDTO> {
        return withContext(Dispatchers.IO) {
            shoppingListDAO.getProductsByShoppingListAndEstablishment(shoppingListId, establishmentName)
        }
    }

    suspend fun getShoppingListDateAndTotalAmount(shoppingListId: Long): ShoppingListTotalDTO? {
        return withContext(Dispatchers.IO) {
            shoppingListDAO.getShoppingListDateAndTotalAmount(shoppingListId)
        }
    }

    fun completeOrCancelShoppingList(userId: Long, shoppingListId: Long, stateName: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val state = stateDAO.getStateByName(stateName)
            val shoppingListState = shoppingListStateDAO.getByShoppingList(shoppingListId)

            if(state != null && shoppingListState != null) {
                shoppingListStateDAO.updateStateById(id = shoppingListState.id, state = state.id)
            }

            if(stateName == TextConstants.STATUS_COMPLETED) {
                val products = productDAO.getActiveProductsWithAmountByUser(userId)
                val productsToAnalyze = shoppingListDAO.getProductsToAnalyzeDTO(shoppingListId)

                products.forEach{ product ->
                    val productToAnalyze = productsToAnalyze.find { productToAnalyzeDTO -> productToAnalyzeDTO.name == product.productName }

                    if(productToAnalyze != null && (productToAnalyze.unitPrice != product.unitPrice || productToAnalyze.amount != product.amountValue)) {

                        productDAO.updateUnitPrice(product.productId, productToAnalyze.unitPrice)
                        amountDAO.updateValueById(product.amountId, productToAnalyze.amount)
                    }
                }
            }

            onSuccess()
        }
    }

    fun updateIsReadyById(productShoppingListId: Long, isReady: Boolean) {
        viewModelScope.launch {
            productShoppingListDAO.updateIsReadyById(productShoppingListId, isReady)
        }
    }


    suspend fun generateShoppingList(date: LocalDateTime, userId: Long, onError: (String) -> Unit, onSuccess: () -> Unit) {
        val products = productDAO.getProductsToAnalyzeDTO(userId)
        val lastShoppingList = shoppingListDAO.getLastShoppingList(userId)
        val allShoppingList = getShoppingListsToAnalyze(userId)

        if (products.isEmpty()) {
            onError(Messages.THERE_ARE_NOT_ACTIVE_PRODUCTS)
            return
        }

        if(lastShoppingList != null) {
            val lastDateShoppingList = lastShoppingList.date
            val weeksBetweenDates = ChronoUnit.WEEKS.between(lastDateShoppingList, date)

            if (date.isBefore(lastDateShoppingList)) {
                onError(Messages.DATE_IS_BEFORE_LAST_SHOPPING_LIST + date.format(DateTimeFormatter.ISO_DATE))
                return
            }

            if (weeksBetweenDates < NumericConstants.POSITIVE_ONE) {
                onError(Messages.DATE_IS_NOT_SEVEN_DAYS_AFTER)
                return
            }

            if (lastShoppingList.status == TextConstants.STATUS_ACTIVE) {
                onError(Messages.THERE_IS_AN_ACTIVE_SHOPPING_LIST)
                return
            }
        }

        viewModelScope.launch {
            val shoppingList = ShoppingList(shoppingListDate = date, user = userId)
            val shoppingListId = shoppingListDAO.insert(shoppingList)

            val state = stateDAO.getStateByName(TextConstants.STATUS_ACTIVE)

            if(state != null) {
                val shoppingListState = ShoppingListState(shoppingList = shoppingListId, state = state.id)

                shoppingListStateDAO.insert(shoppingListState)
            }

            if(allShoppingList.isEmpty()) {
                products.forEach{ product ->
                    val productShoppingList = ProductShoppingList(unitPrice = product.unitPrice, purchasedAmount = product.amount, isReady = false, shoppingList = shoppingListId, product = product.id)

                    productShoppingListDAO.insert(productShoppingList)
                }
            } else {
                products.forEach{ product ->
                    val aShoppingList = findShoppingListByFrequency(allShoppingList, date, product.purchaseFrequency)

                    if(aShoppingList != null) {
                        val aProduct = aShoppingList.products.firstOrNull {it.name == product.name}

                        if(aProduct != null) {
                            val productShoppingList = ProductShoppingList(unitPrice = product.unitPrice, purchasedAmount = product.amount, isReady = false, shoppingList = shoppingListId, product = product.id)

                            productShoppingListDAO.insert(productShoppingList)
                        }
                    } else {
                        if(lastShoppingList != null && lastShoppingList.date.isBefore(date)){
                            val productShoppingList = ProductShoppingList(unitPrice = product.unitPrice, purchasedAmount = product.amount, isReady = false, shoppingList = shoppingListId, product = product.id)

                            productShoppingListDAO.insert(productShoppingList)
                        }
                    }
                }
            }

            onSuccess()
        }
    }

    fun deleteShoppingList(shoppingListId: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val productShoppingLists = productShoppingListDAO.getByShoppingList(shoppingListId)

            productShoppingLists.forEach { product ->
                productShoppingListDAO.deleteById(product.id)
            }

            val shoppingListState = shoppingListStateDAO.getByShoppingList(shoppingListId)

            if(shoppingListState != null) {
                shoppingListStateDAO.deleteById(shoppingListState.id)
            }

            shoppingListDAO.deleteShoppingListById(shoppingListId)

            onSuccess()
        }
    }

    suspend fun getShoppingListsToAnalyze(user: Long): List<ShoppingListToAnalyzeDTO> {
        return withContext(Dispatchers.IO) {
            val shoppingLists = shoppingListDAO.getShoppingListstToAnalyze(user)

            shoppingLists.map { shoppingListDTO ->
                val products = shoppingListDAO.getProductsToAnalyzeDTO(shoppingListDTO.id)
                ShoppingListToAnalyzeDTO(
                    id = shoppingListDTO.id,
                    date = shoppingListDTO.date,
                    status = shoppingListDTO.status,
                    products = products
                )
            }
        }
    }

    fun findShoppingListByFrequency(
        allShoppingList: List<ShoppingListToAnalyzeDTO>,
        date: LocalDateTime,
        purchaseFrequency: String
    ): ShoppingListToAnalyzeDTO? {
        val thresholdDate = getDateThreshold(date, purchaseFrequency)
        return allShoppingList.firstOrNull { it.date.isBefore(thresholdDate) }
    }

    fun getDateThreshold(date: LocalDateTime, purchaseFrequency: String): LocalDateTime = when (purchaseFrequency) {
        TextConstants.FREQUENCY_WEEKLY -> date.minusWeeks(1)
        TextConstants.FREQUENCY_FORTNIGHTLY -> date.minusWeeks(2)
        TextConstants.FREQUENCY_MONTHLY -> date.minusMonths(1)
        TextConstants.FREQUENCY_BIMONTHLY -> date.minusMonths(2)
        TextConstants.FREQUENCY_QUARTERLY -> date.minusMonths(3)
        TextConstants.FREQUENCY_FOUR_MONTHLY -> date.minusMonths(4)
        TextConstants.FREQUENCY_SEMIANNUAL -> date.minusMonths(6)
        else -> throw IllegalArgumentException(Messages.NOT_SUPPORTED_FREQUENCY + purchaseFrequency)
    }
}