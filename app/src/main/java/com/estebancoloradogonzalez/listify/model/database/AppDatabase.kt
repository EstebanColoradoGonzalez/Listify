package com.estebancoloradogonzalez.listify.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.estebancoloradogonzalez.listify.model.dao.BudgetDAO
import com.estebancoloradogonzalez.listify.model.dao.EstablishmentDAO
import com.estebancoloradogonzalez.listify.model.dao.PurchaseFrequencyDAO
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
import com.estebancoloradogonzalez.listify.model.entity.PurchaseFrequency
import com.estebancoloradogonzalez.listify.model.entity.State
import com.estebancoloradogonzalez.listify.model.entity.UnitOfMeasurement
import com.estebancoloradogonzalez.listify.model.entity.User
import com.estebancoloradogonzalez.listify.utils.DateConverter
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
        AmountUnitOfMeasurement::class,
        ProductEstablishment::class,
        ProductPurchaseFrequency::class,
        ProductCategory::class
    ],
    version = 1
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDAO
    abstract fun budgetDao(): BudgetDAO
    abstract fun stateDao(): StateDAO
    abstract fun unitOfMeasurementDao(): UnitOfMeasurementDAO
    abstract fun establishmentDao(): EstablishmentDAO
    abstract fun purchaseFrequencyDao(): PurchaseFrequencyDAO

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
        }
    }
}