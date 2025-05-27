package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.*
import com.estebancoloradogonzalez.listify.model.entity.AmountUnitOfMeasurement
import com.estebancoloradogonzalez.listify.utils.Queries

/**
 * Data Access Object for AmountUnitOfMeasurement entity.
 * Provides CRUD operations for the AmountUnitOfMeasurement table.
 */
@Dao
interface AmountUnitOfMeasurementDAO {
    /**
     * Inserts a new AmountUnitOfMeasurement into the database.
     * @param amountUnit The AmountUnitOfMeasurement entity to insert.
     * @return The row ID of the newly inserted entity, or -1 if it already exists.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(amountUnit: AmountUnitOfMeasurement): Long

    /**
     * Retrieves an AmountUnitOfMeasurement by its ID.
     * @param id The ID of the AmountUnitOfMeasurement.
     * @return The AmountUnitOfMeasurement entity if found, null otherwise.
     */
    @Query(Queries.SELECT_AMOUNT_UNIT_OF_MEASUREMENT_BY_ID)
    suspend fun getById(id: Long): AmountUnitOfMeasurement?

    /**
     * Retrieves an AmountUnitOfMeasurement by its associated amount ID.
     * @param amount The ID of the associated Amount.
     * @return The AmountUnitOfMeasurement entity if found, null otherwise.
     */
    @Query(Queries.SELECT_AMOUNT_UNIT_OF_MEASUREMENT_BY_AMOUNT)
    suspend fun getByAmount(amount: Long): AmountUnitOfMeasurement?

    /**
     * Updates the unit of measurement for a given AmountUnitOfMeasurement by its ID.
     * @param id The ID of the AmountUnitOfMeasurement.
     * @param unitOfMeasurement The new unit of measurement ID.
     * @return The number of rows updated.
     */
    @Query(Queries.UPDATE_AMOUNT_UNIT_OF_MEASUREMENT_UNIT)
    suspend fun updateUnitById(id: Long, unitOfMeasurement: Long): Int

    /**
     * Deletes an AmountUnitOfMeasurement by its ID.
     * @param id The ID of the AmountUnitOfMeasurement to delete.
     * @return The number of rows deleted.
     */
    @Query(Queries.DELETE_AMOUNT_UNIT_OF_MEASUREMENT_BY_ID)
    suspend fun deleteById(id: Long): Int
}