package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.*
import com.estebancoloradogonzalez.listify.model.entity.UnitOfMeasurement
import com.estebancoloradogonzalez.listify.utils.Queries

/**
 * Data Access Object for the UnitOfMeasurement entity.
 * Provides CRUD operations for the UnitOfMeasurement table.
 */
@Dao
interface UnitOfMeasurementDAO {

    /**
     * Inserts or replaces multiple UnitOfMeasurement entities in the database.
     * @param units Vararg of UnitOfMeasurement entities to insert or replace.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg units: UnitOfMeasurement)

    /**
     * Retrieves all UnitOfMeasurement entities from the database.
     * @return List of all UnitOfMeasurement entities.
     */
    @Query(Queries.SELECT_UNIT_OF_MEASUREMENT)
    suspend fun getAll(): List<UnitOfMeasurement>

    /**
     * Retrieves a UnitOfMeasurement by its name.
     * @param name The name of the UnitOfMeasurement.
     * @return The UnitOfMeasurement entity if found, null otherwise.
     */
    @Query(Queries.SELECT_UNIT_OF_MEASUREMENT_BY_NAME)
    suspend fun getByName(name: String): UnitOfMeasurement?

    /**
     * Retrieves a UnitOfMeasurement by its ID.
     * @param id The ID of the UnitOfMeasurement.
     * @return The UnitOfMeasurement entity if found, null otherwise.
     */
    @Query(Queries.SELECT_UNIT_OF_MEASUREMENT_BY_ID)
    suspend fun getById(id: Long): UnitOfMeasurement?

    /**
     * Deletes all UnitOfMeasurement entities from the database.
     */
    @Query(Queries.DELETE_UNITS_OF_MEASUREMENT)
    suspend fun deleteAll()
}