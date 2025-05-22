package com.estebancoloradogonzalez.listify.model.database.service

import com.estebancoloradogonzalez.listify.model.database.AppDatabase
import com.estebancoloradogonzalez.listify.model.database.utils.ProductData
import com.estebancoloradogonzalez.listify.model.dto.ShoppingListToAnalyzeDTO
import com.estebancoloradogonzalez.listify.model.entity.ProductShoppingList
import com.estebancoloradogonzalez.listify.model.entity.ShoppingList
import com.estebancoloradogonzalez.listify.model.entity.ShoppingListState
import com.estebancoloradogonzalez.listify.model.entity.State
import com.estebancoloradogonzalez.listify.utils.Messages
import com.estebancoloradogonzalez.listify.utils.NumericConstants
import com.estebancoloradogonzalez.listify.utils.TextConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class ShoppingListGenerator(private val db: AppDatabase) {
    suspend fun generateTestShoppingLists(userId: Long) {
        generateShoppingList(
            date = LocalDateTime.of(2025, 5, 3, 0, 0),
            userId = userId,
            NumericConstants.LONG_ONE
        )
    }

    suspend fun generateShoppingList(date: LocalDateTime,
                                     userId: Long,
                                     position: Long) {
        val productDAO = db.productDao()
        val shoppingListDAO = db.shoppingListDao()
        val stateDAO = db.stateDao()
        val shoppingListStateDAO = db.shoppingListStateDao()
        val productShoppingListDAO = db.productShoppingListDao()

        val products = productDAO.getProductsToAnalyzeDTO(userId)
        val lastShoppingList = shoppingListDAO.getLastShoppingList(userId)
        val allShoppingList = getShoppingListsToAnalyze(userId, db)

        val shoppingList = ShoppingList(shoppingListDate = date, user = userId)
        val shoppingListId = shoppingListDAO.insert(shoppingList)

        val state : State? = if (position == NumericConstants.LONG_ONE) {
            stateDAO.getStateByName(TextConstants.STATUS_COMPLETED)
        } else {
            stateDAO.getStateByName(TextConstants.STATUS_ACTIVE)
        }

        if(state != null) {
            val shoppingListState = ShoppingListState(shoppingList = shoppingListId, state = state.id)

            shoppingListStateDAO.insert(shoppingListState)
        }

        if(allShoppingList.isEmpty()) {
            products.forEach{ product ->
                if(position == NumericConstants.LONG_ONE && productsNotIncludedInTheFirstPosition(product.name)) {
                    val productShoppingList = ProductShoppingList(unitPrice = product.unitPrice, purchasedAmount = product.amount, isReady = true, shoppingList = shoppingListId, product = product.id)

                    productShoppingListDAO.insert(productShoppingList)
                }
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
                    if(lastShoppingList != null && product.registrationDate.isAfter(lastShoppingList.date)) {
                        val productShoppingList = ProductShoppingList(unitPrice = product.unitPrice, purchasedAmount = product.amount, isReady = false, shoppingList = shoppingListId, product = product.id)

                        productShoppingListDAO.insert(productShoppingList)
                    }
                }
            }
        }
    }

    fun productsNotIncludedInTheFirstPosition(productName: String) : Boolean {
        return productName != ProductData.SalsaBBQ.NAME &&
                productName != ProductData.Ducales.NAME &&
                productName != ProductData.SnackCremoso.NAME &&
                productName != ProductData.LimpiadorBicarbonato.NAME &&
                productName != ProductData.PanoLimpon.NAME &&
                productName != ProductData.Alcohol.NAME &&
                productName != ProductData.PastillasParaBano.NAME &&
                productName != ProductData.Listerine.NAME &&
                productName != ProductData.JabonLiquidoDeManos.NAME &&
                productName != ProductData.EsponjaParaBrillarOllas.NAME &&
                productName != ProductData.EsponjaParaTrastes.NAME &&
                productName != ProductData.AmbientadorDeArenaDeGatos.NAME &&
                productName != ProductData.Aluminio.NAME &&
                productName != ProductData.Brillantismo.NAME &&
                productName != ProductData.Alitas.NAME &&
                productName != ProductData.Lagarto.NAME &&
                productName != ProductData.Mango.NAME &&
                productName != ProductData.Pimenton.NAME &&
                productName != ProductData.Repollo.NAME &&
                productName != ProductData.Arverja.NAME &&
                productName != ProductData.Pimienta.NAME &&
                productName != ProductData.CremaDepilacion.NAME &&
                productName != ProductData.Shampoo.NAME &&
                productName != ProductData.BolsasTransparentes.NAME &&
                productName != ProductData.Mayonesa.NAME &&
                productName != ProductData.ArenaParaLaGata.NAME
    }

    suspend fun getShoppingListsToAnalyze(user: Long, db: AppDatabase): List<ShoppingListToAnalyzeDTO> {
        val shoppingListDAO = db.shoppingListDao()

        return withContext(Dispatchers.IO) {
            val shoppingLists = shoppingListDAO.getShoppingListsToAnalyze(user)

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
        TextConstants.FREQUENCY_WEEKLY -> date.minusWeeks(NumericConstants.LONG_ONE)
        TextConstants.FREQUENCY_FORTNIGHTLY -> date.minusWeeks(NumericConstants.LONG_TWO)
        TextConstants.FREQUENCY_MONTHLY -> date.minusWeeks(NumericConstants.LONG_FOUR)
        TextConstants.FREQUENCY_BIMONTHLY -> date.minusWeeks(NumericConstants.LONG_EIGHT)
        TextConstants.FREQUENCY_QUARTERLY -> date.minusWeeks(NumericConstants.LONG_TWELVE)
        TextConstants.FREQUENCY_FOUR_MONTHLY -> date.minusWeeks(NumericConstants.LONG_SIXTEEN)
        TextConstants.FREQUENCY_SEMIANNUAL -> date.minusWeeks(NumericConstants.LONG_TWENTY_FOUR)
        else -> throw IllegalArgumentException(Messages.NOT_SUPPORTED_FREQUENCY + purchaseFrequency)
    }
}
