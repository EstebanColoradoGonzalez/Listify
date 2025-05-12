package com.estebancoloradogonzalez.listify.model.database

import android.content.Context
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
import com.estebancoloradogonzalez.listify.utils.NumericConstants
import com.estebancoloradogonzalez.listify.utils.TextConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

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
            val userId = registerUser("Esteban Colorado González", "870000", db)

            registerProduct("Tocineta", "6300", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_D1, TextConstants.MEATS_AND_PROTEINS, userId, db)
            registerProduct("Jamon", "8500", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_D1, TextConstants.MEATS_AND_PROTEINS, userId, db)
            registerProduct("Pan Tajado", "6490", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_D1, TextConstants.BAKERY, userId, db)
            registerProduct("Queso Mozarella en Lonchas", "9490", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_D1, TextConstants.DAIRY_AND_DERIVATIVES, userId, db)
            registerProduct("Queso Crema", "3300", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_D1, TextConstants.DAIRY_AND_DERIVATIVES, userId, db)
            registerProduct("Salsa BBQ", "6800", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_BIMONTHLY, TextConstants.STORE_D1, TextConstants.SAUCES_AND_DRESSINGS, userId, db)
            registerProduct("Salsa Aguacate", "3490", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_BIMONTHLY, TextConstants.STORE_D1, TextConstants.SAUCES_AND_DRESSINGS, userId, db)
            registerProduct("Salchichas", "6100", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_D1, TextConstants.MEATS_AND_PROTEINS, userId, db)
            registerProduct("Crema Dental", "5850", "2", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_BIMONTHLY, TextConstants.STORE_D1, TextConstants.CLEANING_AND_HOME, userId, db)
            registerProduct("Suavizante", "8990", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_D1, TextConstants.CLEANING_AND_HOME, userId, db)
            registerProduct("Galletas para Niebla", "2200", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_D1, TextConstants.PETS, userId, db)
            registerProduct("Galletas para Nube", "2900", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_D1, TextConstants.PETS, userId, db)
            registerProduct("Atún", "3450", "2", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_D1, TextConstants.MEATS_AND_PROTEINS, userId, db)
            registerProduct("Aceite", "27950", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_D1, TextConstants.OILS_AND_FATS, userId, db)
            registerProduct("Nescafe", "12650", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_D1, TextConstants.BEVERAGES, userId, db)
            registerProduct("Ducales", "5700", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_D1, TextConstants.SNACKS, userId, db)
            registerProduct("Crema de Leche", "2190", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_D1, TextConstants.DAIRY_AND_DERIVATIVES, userId, db)
            registerProduct("Mantequilla", "4700", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_D1, TextConstants.DAIRY_AND_DERIVATIVES, userId, db)
            registerProduct("Azucar", "3990", "2", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_D1, TextConstants.SWEETENERS, userId, db)
            registerProduct("Sal", "2490", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_D1, TextConstants.SPICES_AND_CONDIMENTS, userId, db)
            registerProduct("Snack Cremoso", "7990", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_D1, TextConstants.PETS, userId, db)
            registerProduct("Bolsas de Basura Pequeñas", "2190", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_D1, TextConstants.CLEANING_AND_HOME, userId, db)
            registerProduct("Bolsas de Basura Grandes", "2200", "2", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_D1, TextConstants.CLEANING_AND_HOME, userId, db)
            registerProduct("Detergente Ropa Oscura", "6300", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_D1, TextConstants.CLEANING_AND_HOME, userId, db)
            registerProduct("Detergente Liquido", "12600", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_D1, TextConstants.CLEANING_AND_HOME, userId, db)
            registerProduct("Jabón tocador", "5990", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_D1, TextConstants.CLEANING_AND_HOME, userId, db)
            registerProduct("Ramén", "2490", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_D1, TextConstants.GRAINS_AND_CEREALS, userId, db)
            registerProduct("Papel Higenico", "13940", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_BIMONTHLY, TextConstants.STORE_D1, TextConstants.CLEANING_AND_HOME, userId, db)
            registerProduct("Limpiavidrios", "2250", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_QUARTERLY, TextConstants.STORE_D1, TextConstants.CLEANING_AND_HOME, userId, db)
            registerProduct("Limpiador Bicarbonato", "2600", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_QUARTERLY, TextConstants.STORE_D1, TextConstants.CLEANING_AND_HOME, userId, db)
            registerProduct("Paño Limpon", "3400", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_QUARTERLY, TextConstants.STORE_D1, TextConstants.CLEANING_AND_HOME, userId, db)
            registerProduct("Alcohol", "2900", "3", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_QUARTERLY, TextConstants.STORE_D1, TextConstants.CLEANING_AND_HOME, userId, db)
            registerProduct("Pastillas para baño", "2900", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_BIMONTHLY, TextConstants.STORE_D1, TextConstants.CLEANING_AND_HOME, userId, db)

            registerProduct("Desodorante", "12000", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_BIMONTHLY, TextConstants.STORE_DOLLAR_CITY, TextConstants.CLEANING_AND_HOME, userId, db)
            registerProduct("Listerine", "14000", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_BIMONTHLY, TextConstants.STORE_DOLLAR_CITY, TextConstants.CLEANING_AND_HOME, userId, db)
            registerProduct("Jabón liquido de manos", "25000", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_SEMIANNUAL, TextConstants.STORE_DOLLAR_CITY, TextConstants.CLEANING_AND_HOME, userId, db)
            registerProduct("Esponja para brillar ollas", "3000", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_QUARTERLY, TextConstants.STORE_DOLLAR_CITY, TextConstants.CLEANING_AND_HOME, userId, db)
            registerProduct("Esponja para trastes", "5000", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_DOLLAR_CITY, TextConstants.CLEANING_AND_HOME, userId, db)
            registerProduct("Limpido", "8000", "2", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_DOLLAR_CITY, TextConstants.CLEANING_AND_HOME, userId, db)
            registerProduct("Brillantismo", "6000", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_QUARTERLY, TextConstants.STORE_DOLLAR_CITY, TextConstants.CLEANING_AND_HOME, userId, db)
            registerProduct("Jabón para Trastes", "12000", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_BIMONTHLY, TextConstants.STORE_DOLLAR_CITY, TextConstants.CLEANING_AND_HOME, userId, db)
            registerProduct("Ambientador de arena de gatos", "6000", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_DOLLAR_CITY, TextConstants.PETS, userId, db)
            registerProduct("Aluminio", "6000", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_DOLLAR_CITY, TextConstants.OTHERS, userId, db)

            registerProduct("Carne de Res", "34000", "1.81", TextConstants.UNIT_KILOGRAM, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_BUTCHER, TextConstants.MEATS_AND_PROTEINS, userId, db)
            registerProduct("Chorizos", "2500", "4", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_BUTCHER, TextConstants.MEATS_AND_PROTEINS, userId, db)
            registerProduct("Solomito de Cerdo", "21000", "0.9", TextConstants.UNIT_KILOGRAM, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_BUTCHER, TextConstants.MEATS_AND_PROTEINS, userId, db)
            registerProduct("Alitas", "17000", "1.36", TextConstants.UNIT_KILOGRAM, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_BUTCHER, TextConstants.MEATS_AND_PROTEINS, userId, db)
            registerProduct("Lagarto", "25000", "1.36", TextConstants.UNIT_KILOGRAM, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_BUTCHER, TextConstants.MEATS_AND_PROTEINS, userId, db)
            registerProduct("Chicharrón", "28000", "1.81", TextConstants.UNIT_KILOGRAM, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_BUTCHER, TextConstants.MEATS_AND_PROTEINS, userId, db)

            registerProduct("Maracuya", "4000", "2.4", TextConstants.UNIT_KILOGRAM, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_GREENGROCER, TextConstants.FRUITS, userId, db)
            registerProduct("Mango", "3800", "3.5", TextConstants.UNIT_KILOGRAM, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_GREENGROCER, TextConstants.FRUITS, userId, db)
            registerProduct("Ajo", "7000", "0.2", TextConstants.UNIT_KILOGRAM, TextConstants.FREQUENCY_BIMONTHLY, TextConstants.STORE_GREENGROCER, TextConstants.VEGETABLES, userId, db)
            registerProduct("Pimenton", "2500", "1", TextConstants.UNIT_KILOGRAM, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_GREENGROCER, TextConstants.VEGETABLES, userId, db)
            registerProduct("Apio", "14000", "0.2", TextConstants.UNIT_KILOGRAM, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_GREENGROCER, TextConstants.VEGETABLES, userId, db)
            registerProduct("Platano", "2800", "1.4", TextConstants.UNIT_KILOGRAM, TextConstants.FREQUENCY_WEEKLY, TextConstants.STORE_GREENGROCER, TextConstants.VEGETABLES, userId, db)
            registerProduct("Pepino", "3000", "1", TextConstants.UNIT_KILOGRAM, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_GREENGROCER, TextConstants.VEGETABLES, userId, db)
            registerProduct("Tomate", "2500", "3", TextConstants.UNIT_KILOGRAM, TextConstants.FREQUENCY_FORTNIGHTLY, TextConstants.STORE_GREENGROCER, TextConstants.VEGETABLES, userId, db)
            registerProduct("Lechuga", "2000", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_FORTNIGHTLY, TextConstants.STORE_GREENGROCER, TextConstants.VEGETABLES, userId, db)
            registerProduct("Zanahoria", "1800", "2", TextConstants.UNIT_KILOGRAM, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_GREENGROCER, TextConstants.VEGETABLES, userId, db)
            registerProduct("Cebolla de Huevo", "5300", "2", TextConstants.UNIT_KILOGRAM, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_GREENGROCER, TextConstants.VEGETABLES, userId, db)
            registerProduct("Papas criollas", "5000", "1", TextConstants.UNIT_KILOGRAM, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_GREENGROCER, TextConstants.VEGETABLES, userId, db)
            registerProduct("Limones", "3500", "2.3", TextConstants.UNIT_KILOGRAM, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_GREENGROCER, TextConstants.FRUITS, userId, db)
            registerProduct("Naranjas", "3000", "2.8", TextConstants.UNIT_KILOGRAM, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_GREENGROCER, TextConstants.FRUITS, userId, db)
            registerProduct("Repollo", "3380", "1", TextConstants.UNIT_KILOGRAM, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_GREENGROCER, TextConstants.VEGETABLES, userId, db)
            registerProduct("Papas", "3000", "7", TextConstants.UNIT_KILOGRAM, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_GREENGROCER, TextConstants.VEGETABLES, userId, db)
            registerProduct("Cilantro", "7000", "0.29", TextConstants.UNIT_KILOGRAM, TextConstants.FREQUENCY_FORTNIGHTLY, TextConstants.STORE_GREENGROCER, TextConstants.VEGETABLES, userId, db)
            registerProduct("Arverja", "6500", "0.62", TextConstants.UNIT_KILOGRAM, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_GREENGROCER, TextConstants.VEGETABLES, userId, db)
            registerProduct("Sal de Ajo", "1000", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_SEMIANNUAL, TextConstants.STORE_GREENGROCER, TextConstants.SPICES_AND_CONDIMENTS, userId, db)
            registerProduct("Pimienta", "1000", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_SEMIANNUAL, TextConstants.STORE_GREENGROCER, TextConstants.SPICES_AND_CONDIMENTS, userId, db)
            registerProduct("Oregano en Polvo", "2000", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_SEMIANNUAL, TextConstants.STORE_GREENGROCER, TextConstants.SPICES_AND_CONDIMENTS, userId, db)
            registerProduct("Sal de Cebolla", "1000", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_SEMIANNUAL, TextConstants.STORE_GREENGROCER, TextConstants.SPICES_AND_CONDIMENTS, userId, db)

            registerProduct("Shampoo", "40000", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_BIMONTHLY, TextConstants.STORE_OTHER, TextConstants.CLEANING_AND_HOME, userId, db)
            registerProduct("Crema Depilación", "30000", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_OTHER, TextConstants.CLEANING_AND_HOME, userId, db)
            registerProduct("Comida para Niebla", "12500", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_OTHER, TextConstants.PETS, userId, db)
            registerProduct("Comida para Nube", "18000", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_OTHER, TextConstants.PETS, userId, db)
            registerProduct("Leche", "3500", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_OTHER, TextConstants.DAIRY_AND_DERIVATIVES, userId, db)
            registerProduct("Salchichon Cervecero", "22000", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_OTHER, TextConstants.MEATS_AND_PROTEINS, userId, db)
            registerProduct("Bolsas Transparentes", "8000", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_SEMIANNUAL, TextConstants.STORE_OTHER, TextConstants.CLEANING_AND_HOME, userId, db)
            registerProduct("Arroz", "2100", "6", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_OTHER, TextConstants.GRAINS_AND_CEREALS, userId, db)
            registerProduct("Salsa de Tomate", "16000", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_OTHER, TextConstants.SAUCES_AND_DRESSINGS, userId, db)
            registerProduct("Mayonesa", "16000", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_OTHER, TextConstants.SAUCES_AND_DRESSINGS, userId, db)
            registerProduct("Huevos", "17000", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_FORTNIGHTLY, TextConstants.STORE_OTHER, TextConstants.MEATS_AND_PROTEINS, userId, db)
            registerProduct("Arena para la Gata", "24000", "1", TextConstants.UNIT_COUNT, TextConstants.FREQUENCY_MONTHLY, TextConstants.STORE_OTHER, TextConstants.PETS, userId, db)
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

            val product = Product(name = productName, unitPrice = productPrice.toDouble(), isActive = true, amount = amountId, user = userId, registrationDate = LocalDateTime.now())
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
    }
}