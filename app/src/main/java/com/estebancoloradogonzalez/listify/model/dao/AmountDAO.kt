package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.*
import com.estebancoloradogonzalez.listify.model.entity.Amount
import com.estebancoloradogonzalez.listify.utils.Queries

/**
 * Data Access Object for the Amount entity.
 * Provides methods to perform CRUD operations on the Amount table.
 */
@Dao
interface AmountDAO {
    /**
     * Inserts a new Amount into the database.
     * @param amount The Amount entity to insert.
     * @return The row ID of the newly inserted Amount, or -1 if already exists.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(amount: Amount): Long

    /**
     * Retrieves an Amount by its ID.
     * @param id The ID of the Amount.
     * @return The Amount entity if found, null otherwise.
     */
    @Query(Queries.SELECT_AMOUNT_BY_ID)
    suspend fun getById(id: Long): Amount?

    /**
     * Updates the value of an Amount by its ID.
     * @param id The ID of the Amount.
     * @param value The new value to set.
     * @return The number of rows updated.
     */
    @Query(Queries.UPDATE_AMOUNT)
    suspend fun updateValueById(id: Long, value: Double): Int

    /**
     * Deletes an Amount by its ID.
     * @param id The ID of the Amount to delete.
     * @return The number of rows deleted.
     */
    @Query(Queries.DELETE_AMOUNT)
    suspend fun deleteById(id: Long): Int
}