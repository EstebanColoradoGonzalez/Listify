package com.estebancoloradogonzalez.listify.model.database

import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.estebancoloradogonzalez.listify.model.dao.AmountDAO
import com.estebancoloradogonzalez.listify.model.dao.AmountUnitOfMeasurementDAO
import com.estebancoloradogonzalez.listify.model.dao.BudgetDAO
import com.estebancoloradogonzalez.listify.model.dao.CategoryDAO
import com.estebancoloradogonzalez.listify.model.dao.EstablishmentDAO
import com.estebancoloradogonzalez.listify.model.dao.ProductCategoryDAO
import com.estebancoloradogonzalez.listify.model.dao.ProductDAO
import com.estebancoloradogonzalez.listify.model.dao.ProductEstablishmentDAO
import com.estebancoloradogonzalez.listify.model.dao.ProductPurchaseFrenquencyDAO
import com.estebancoloradogonzalez.listify.model.dao.ProductShoppingListDAO
import com.estebancoloradogonzalez.listify.model.dao.PurchaseFrequencyDAO
import com.estebancoloradogonzalez.listify.model.dao.ShoppingListDAO
import com.estebancoloradogonzalez.listify.model.dao.ShoppingListStateDAO
import com.estebancoloradogonzalez.listify.model.dao.StateDAO
import com.estebancoloradogonzalez.listify.model.dao.UnitOfMeasurementDAO
import com.estebancoloradogonzalez.listify.model.dao.UserDAO
import com.estebancoloradogonzalez.listify.model.database.utils.ProductData
import com.estebancoloradogonzalez.listify.model.database.utils.ProductsSeed
import com.estebancoloradogonzalez.listify.model.database.utils.UserData
import com.estebancoloradogonzalez.listify.model.dto.ShoppingListToAnalyzeDTO
import com.estebancoloradogonzalez.listify.model.entity.Amount
import com.estebancoloradogonzalez.listify.model.entity.AmountUnitOfMeasurement
import com.estebancoloradogonzalez.listify.model.entity.Budget
import com.estebancoloradogonzalez.listify.model.entity.Category
import com.estebancoloradogonzalez.listify.model.entity.Establishment
import com.estebancoloradogonzalez.listify.model.entity.Product
import com.estebancoloradogonzalez.listify.model.entity.ProductCategory
import com.estebancoloradogonzalez.listify.model.entity.ProductEstablishment
import com.estebancoloradogonzalez.listify.model.entity.ProductPurchaseFrequency
import com.estebancoloradogonzalez.listify.model.entity.ProductShoppingList
import com.estebancoloradogonzalez.listify.model.entity.PurchaseFrequency
import com.estebancoloradogonzalez.listify.model.entity.ShoppingList
import com.estebancoloradogonzalez.listify.model.entity.ShoppingListState
import com.estebancoloradogonzalez.listify.model.entity.State
import com.estebancoloradogonzalez.listify.model.entity.UnitOfMeasurement
import com.estebancoloradogonzalez.listify.model.entity.User
import com.estebancoloradogonzalez.listify.utils.DateConverter
import com.estebancoloradogonzalez.listify.utils.Messages
import com.estebancoloradogonzalez.listify.utils.NumericConstants
import com.estebancoloradogonzalez.listify.utils.TextConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Database(
    entities = [
        User::class,
        Budget::class,
        State::class,
        UnitOfMeasurement::class,
        Establishment::class,
        PurchaseFrequency::class,
        Amount::class,
        Category::class,
        Product::class,
        ShoppingList::class,
        ShoppingListState::class,
        ProductShoppingList::class,
        AmountUnitOfMeasurement::class,
        ProductEstablishment::class,
        ProductPurchaseFrequency::class,
        ProductCategory::class
    ],
    version = NumericConstants.POSITIVE_ONE
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun amountDao(): AmountDAO
    abstract fun amountUnitOfMeasurementDao(): AmountUnitOfMeasurementDAO
    abstract fun budgetDao(): BudgetDAO
    abstract fun categoryDao(): CategoryDAO
    abstract fun establishmentDao(): EstablishmentDAO
    abstract fun productCategoryDao(): ProductCategoryDAO
    abstract fun productDao(): ProductDAO
    abstract fun shoppingListDao(): ShoppingListDAO
    abstract fun shoppingListStateDao(): ShoppingListStateDAO
    abstract fun productShoppingListDao(): ProductShoppingListDAO
    abstract fun productEstablishmentDao(): ProductEstablishmentDAO
    abstract fun productPurchaseFrenquencyDao(): ProductPurchaseFrenquencyDAO
    abstract fun purchaseFrequencyDao(): PurchaseFrequencyDAO
    abstract fun stateDao(): StateDAO
    abstract fun unitOfMeasurementDao(): UnitOfMeasurementDAO
    abstract fun userDao(): UserDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    TextConstants.DATABASE_NAME
                ).addCallback(DatabaseCallback(context)).build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(private val context: Context) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO).launch {
                val database = getDatabase(context)
                prepopulateDatabase(database)
            }
        }

        suspend fun prepopulateDatabase(db: AppDatabase) {
            val stateDao = db.stateDao()
            val unitDao = db.unitOfMeasurementDao()
            val establishmentDao = db.establishmentDao()
            val frequencyDao = db.purchaseFrequencyDao()
            val categoryDao = db.categoryDao()

            stateDao.deleteAll()
            unitDao.deleteAll()
            establishmentDao.deleteAll()
            frequencyDao.deleteAll()

            stateDao.insertAll(
                State(name = TextConstants.STATUS_ACTIVE),
                State(name = TextConstants.STATUS_COMPLETED),
                State(name = TextConstants.STATUS_CANCELLED)
            )

            unitDao.insertAll(
                UnitOfMeasurement(name = TextConstants.UNIT_KILOGRAM, symbol = TextConstants.UNIT_KILOGRAM_SHORT),
                UnitOfMeasurement(name = TextConstants.UNIT_POUND, symbol = TextConstants.UNIT_POUND_SHORT),
                UnitOfMeasurement(name = TextConstants.UNIT_LITER, symbol = TextConstants.UNIT_LITER_SHORT),
                UnitOfMeasurement(name = TextConstants.UNIT_COUNT, symbol = TextConstants.UNIT_COUNT_SHORT)
            )

            establishmentDao.insertAll(
                Establishment(name = TextConstants.STORE_D1),
                Establishment(name = TextConstants.STORE_DOLLAR_CITY),
                Establishment(name = TextConstants.STORE_BUTCHER),
                Establishment(name = TextConstants.STORE_GREENGROCER),
                Establishment(name = TextConstants.STORE_OTHER)
            )

            frequencyDao.insertAll(
                PurchaseFrequency(name = TextConstants.FREQUENCY_WEEKLY),
                PurchaseFrequency(name = TextConstants.FREQUENCY_FORTNIGHTLY),
                PurchaseFrequency(name = TextConstants.FREQUENCY_MONTHLY),
                PurchaseFrequency(name = TextConstants.FREQUENCY_BIMONTHLY),
                PurchaseFrequency(name = TextConstants.FREQUENCY_QUARTERLY),
                PurchaseFrequency(name = TextConstants.FREQUENCY_FOUR_MONTHLY),
                PurchaseFrequency(name = TextConstants.FREQUENCY_SEMIANNUAL)
            )

            categoryDao.insertAll(
                Category(name = TextConstants.FRUITS),
                Category(name = TextConstants.VEGETABLES),
                Category(name = TextConstants.SPICES_AND_CONDIMENTS),
                Category(name = TextConstants.BEVERAGES),
                Category(name = TextConstants.GRAINS_AND_CEREALS),
                Category(name = TextConstants.MEATS_AND_PROTEINS),
                Category(name = TextConstants.DAIRY_AND_DERIVATIVES),
                Category(name = TextConstants.BAKERY),
                Category(name = TextConstants.SAUCES_AND_DRESSINGS),
                Category(name = TextConstants.SWEETENERS),
                Category(name = TextConstants.OILS_AND_FATS),
                Category(name = TextConstants.SNACKS),
                Category(name = TextConstants.CLEANING_AND_HOME),
                Category(name = TextConstants.PETS),
                Category(name = TextConstants.OTHERS),
            )
            registerProducts(db)
        }

        suspend fun registerProducts(db: AppDatabase) {
            val userId = registerUser(UserData.USER_NAME, UserData.USER_BUDGET, db)

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

            generateShoppingLists(userId, db)
        }


        suspend fun registerUser(name: String, budget: String, db: AppDatabase) : Long {
            val budgetDao = db.budgetDao()
            val userDao = db.userDao()

            val newBudget = Budget(value = budget.toDouble())
            budgetDao.insertBudget(newBudget)

            val budgetId = budgetDao.getBudgetId()

            val newUser = User(name = name, registrationDate = LocalDateTime.now(), budget = budgetId)
            userDao.insertUser(newUser)

            return userDao.getUserId()
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

        fun isNotActiveProduct(productName: String) : Boolean {
            return productName != ProductData.Nescafe.NAME &&
                    productName != ProductData.Ramen.NAME &&
                    productName != ProductData.SolomitoDeCerdo.NAME &&
                    productName != ProductData.PapasCriollas.NAME
        }

        suspend fun generateShoppingLists(userId: Long,
                                          db: AppDatabase) {
            generateShoppingList(
                date = LocalDateTime.of(2025, 5, 3, 0, 0),
                userId = userId,
                db,
                NumericConstants.LONG_ONE
            )
        }

        suspend fun generateShoppingList(date: LocalDateTime,
                                         userId: Long,
                                         db: AppDatabase,
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

            val state = stateDAO.getStateByName(TextConstants.STATUS_ACTIVE)

            if(state != null) {
                val shoppingListState = ShoppingListState(shoppingList = shoppingListId, state = state.id)

                shoppingListStateDAO.insert(shoppingListState)
            }

            if(allShoppingList.isEmpty()) {
                products.forEach{ product ->
                    if(position == NumericConstants.LONG_ONE && productsNotIncludedInTheFirstPosition(product.name)) {
                        val productShoppingList = ProductShoppingList(unitPrice = product.unitPrice, purchasedAmount = product.amount, isReady = false, shoppingList = shoppingListId, product = product.id)

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
                        if(lastShoppingList != null && lastShoppingList.date.isBefore(date)){
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
            TextConstants.FREQUENCY_MONTHLY -> date.minusMonths(NumericConstants.LONG_ONE)
            TextConstants.FREQUENCY_BIMONTHLY -> date.minusMonths(NumericConstants.LONG_TWO)
            TextConstants.FREQUENCY_QUARTERLY -> date.minusMonths(NumericConstants.LONG_THREE)
            TextConstants.FREQUENCY_FOUR_MONTHLY -> date.minusMonths(NumericConstants.LONG_FOUR)
            TextConstants.FREQUENCY_SEMIANNUAL -> date.minusMonths(NumericConstants.LONG_SIX)
            else -> throw IllegalArgumentException(Messages.NOT_SUPPORTED_FREQUENCY + purchaseFrequency)
        }
    }
}