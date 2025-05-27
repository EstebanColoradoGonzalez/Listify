package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.*
import com.estebancoloradogonzalez.listify.model.entity.Establishment
import com.estebancoloradogonzalez.listify.utils.Queries

/**
 * Data Access Object for the Establishment entity.
 * Provides methods to perform CRUD operations on the Establishment table.
 */
@Dao
interface EstablishmentDAO {
    /**
     * Inserts or replaces multiple Establishment entities in the database.
     * @param establishments Vararg of Establishment entities to insert or replace.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg establishments: Establishment)

    /**
     * Retrieves all Establishment entities from the database.
     * @return List of all Establishment entities.
     */
    @Query(Queries.SELECT_ESTABLISHMENTS)
    suspend fun getAll(): List<Establishment>

    /**
     * Retrieves an Establishment by its name.
     * @param name The name of the Establishment.
     * @return The Establishment entity if found, null otherwise.
     */
    @Query(Queries.SELECT_ESTABLISHMENT_BY_NAME)
    suspend fun getByName(name: String): Establishment?

    /**
     * Deletes all Establishment entities from the database.
     */
    @Query(Queries.DELETE_ESTABLISHMENTS)
    suspend fun deleteAll()
}