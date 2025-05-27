package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.*
import com.estebancoloradogonzalez.listify.model.entity.User
import com.estebancoloradogonzalez.listify.utils.Queries

/**
 * Data Access Object for the User entity.
 * Provides CRUD operations for the User table.
 */
@Dao
interface UserDAO {
    /**
     * Inserts or replaces a User in the database.
     * @param user The User entity to insert or replace.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    /**
     * Retrieves the User entity from the database.
     * @return The User entity if found, null otherwise.
     */
    @Query(Queries.SELECT_USER)
    suspend fun get(): User?

    /**
     * Retrieves the ID of the stored User.
     * @return The ID of the User.
     */
    @Query(Queries.SELECT_USER_ID)
    suspend fun getId(): Long

    /**
     * Updates the name of a User by its ID.
     * @param userId The ID of the User.
     * @param newName The new name for the User.
     */
    @Query(Queries.UPDATE_USER)
    suspend fun updateNameById(userId: Long, newName: String)
}
