package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.*
import com.estebancoloradogonzalez.listify.model.entity.Category
import com.estebancoloradogonzalez.listify.utils.Queries

/**
 * Data Access Object for the Category entity.
 * Provides CRUD operations for the Category table.
 */
@Dao
interface CategoryDAO {
    /**
     * Inserts or replaces a single Category in the database.
     * @param category The Category entity to insert or replace.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category)

    /**
     * Inserts or replaces multiple Category entities in the database.
     * @param categories Vararg of Category entities to insert or replace.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg categories: Category)

    /**
     * Retrieves all Category entities from the database.
     * @return List of all Category entities.
     */
    @Query(Queries.SELECT_CATEGORIES)
    suspend fun getAll(): List<Category>

    /**
     * Retrieves a Category by its ID.
     * @param id The ID of the Category.
     * @return The Category entity if found, null otherwise.
     */
    @Query(Queries.SELECT_CATEGORY_BY_ID)
    suspend fun getById(id: Long): Category?

    /**
     * Retrieves a Category by its name.
     * @param name The name of the Category.
     * @return The Category entity if found, null otherwise.
     */
    @Query(Queries.SELECT_CATEGORY_BY_NAME)
    suspend fun getByName(name: String): Category?

    /**
     * Updates the name of a Category by its ID.
     * @param id The ID of the Category.
     * @param newName The new name for the Category.
     */
    @Query(Queries.UPDATE_CATEGORY)
    suspend fun updateNameById(id: Long, newName: String)

    /**
     * Deletes a Category by its ID.
     * @param id The ID of the Category to delete.
     */
    @Query(Queries.DELETE_CATEGORY)
    suspend fun deleteById(id: Long)

    /**
     * Deletes all Category entities from the database.
     */
    @Query(Queries.DELETE_CATEGORIES)
    suspend fun deleteAll()
}