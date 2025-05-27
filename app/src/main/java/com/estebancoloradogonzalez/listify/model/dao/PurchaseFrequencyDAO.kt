package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.*
import com.estebancoloradogonzalez.listify.model.entity.PurchaseFrequency
import com.estebancoloradogonzalez.listify.utils.Queries

/**
 * Data Access Object for the PurchaseFrequency entity.
 * Provides CRUD operations for the PurchaseFrequency table.
 */
@Dao
interface PurchaseFrequencyDAO {
    /**
     * Inserts or replaces multiple PurchaseFrequency entities in the database.
     * @param frequencies Vararg of PurchaseFrequency entities to insert or replace.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg frequencies: PurchaseFrequency)

    /**
     * Retrieves all PurchaseFrequency entities from the database.
     * @return List of all PurchaseFrequency entities.
     */
    @Query(Queries.SELECT_PURCHASE_FREQUENCY)
    suspend fun getAll(): List<PurchaseFrequency>

    /**
     * Retrieves a PurchaseFrequency by its name.
     * @param name The name of the PurchaseFrequency.
     * @return The PurchaseFrequency entity if found, null otherwise.
     */
    @Query(Queries.SELECT_PURCHASE_FREQUENCY_BY_NAME)
    suspend fun getByName(name: String): PurchaseFrequency?

    /**
     * Deletes all PurchaseFrequency entities from the database.
     */
    @Query(Queries.DELETE_PURCHASE_FREQUENCIES)
    suspend fun deleteAll()
}