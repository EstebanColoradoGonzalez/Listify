package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.*
import com.estebancoloradogonzalez.listify.model.entity.State
import com.estebancoloradogonzalez.listify.utils.Queries

/**
 * Data Access Object for the State entity.
 * Provides CRUD operations for the State table.
 */
@Dao
interface StateDAO {
    /**
     * Inserts or replaces multiple State entities in the database.
     * @param states Vararg of State entities to insert or replace.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg states: State)

    /**
     * Retrieves a State by its name.
     * @param name The name of the State.
     * @return The State entity if found, null otherwise.
     */
    @Query(Queries.SELECT_STATE_BY_NAME)
    suspend fun getByName(name: String): State?

    /**
     * Deletes all State entities from the database.
     */
    @Query(Queries.DELETE_STATES)
    suspend fun deleteAll()
}