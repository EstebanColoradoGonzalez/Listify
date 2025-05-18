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
import com.estebancoloradogonzalez.listify.model.database.prepopulate.PrepopulateFacade
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
                PrepopulateFacade(database).prepopulateDatabase()
            }
        }
    }
}