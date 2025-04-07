package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.*
import com.estebancoloradogonzalez.listify.model.entity.AmountUnitOfMeasurement
import com.estebancoloradogonzalez.listify.utils.Queries

@Dao
interface AmountUnitOfMeasurementDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(amountUnit: AmountUnitOfMeasurement): Long

    @Query(Queries.SELECT_AMOUNT_UNIT_OF_MEASUREMENT_BY_ID)
    suspend fun getById(id: Long): AmountUnitOfMeasurement?

    @Query(Queries.SELECT_AMOUNT_UNIT_OF_MEASUREMENT_BY_AMOUNT)
    suspend fun getByAmount(amount: Long): AmountUnitOfMeasurement?

    @Query(Queries.UPDATE_AMOUNT_UNIT_OF_MEASUREMENT_UNIT)
    suspend fun updateUnitById(id: Long, unitOfMeasurement: Long): Int

    @Query(Queries.DELETE_AMOUNT_UNIT_OF_MEASUREMENT_BY_ID)
    suspend fun deleteById(id: Long): Int
}
