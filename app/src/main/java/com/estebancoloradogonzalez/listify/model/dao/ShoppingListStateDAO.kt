package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.*
import com.estebancoloradogonzalez.listify.model.entity.ShoppingListState
import com.estebancoloradogonzalez.listify.utils.Queries

/**
 * Data Access Object for the ShoppingListState entity.
 * Provides CRUD operations for the ShoppingListState table.
 */
@Dao
interface ShoppingListStateDAO {
    /**
     * Inserts a new ShoppingListState into the database.
     * @param shoppingListState The ShoppingListState entity to insert.
     * @return The row ID of the newly inserted entity, or -1 if it already exists.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(shoppingListState: ShoppingListState): Long

    /**
     * Retrieves a ShoppingListState by its ID.
     * @param id The ID of the ShoppingListState.
     * @return The ShoppingListState entity if found, null otherwise.
     */
    @Query(Queries.SELECT_SHOPPING_LIST_STATE_BY_ID)
    suspend fun getById(id: Long): ShoppingListState?

    /**
     * Retrieves a ShoppingListState by the associated shopping list ID.
     * @param shoppingList The ID of the ShoppingList.
     * @return The ShoppingListState entity if found, null otherwise.
     */
    @Query(Queries.SELECT_SHOPPING_LIST_STATE_BY_SHOPPING_LIST)
    suspend fun getByShoppingList(shoppingList: Long): ShoppingListState?

    /**
     * Updates the state of a ShoppingListState by its ID.
     * @param id The ID of the ShoppingListState.
     * @param state The new state value.
     * @return The number of rows updated.
     */
    @Query(Queries.UPDATE_SHOPPING_LIST_STATE)
    suspend fun updateStateById(id: Long, state: Long): Int

    /**
     * Deletes a ShoppingListState by its ID.
     * @param id The ID of the ShoppingListState to delete.
     * @return The number of rows deleted.
     */
    @Query(Queries.DELETE_SHOPPING_LIST_STATE_BY_ID)
    suspend fun deleteById(id: Long): Int
}