package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.estebancoloradogonzalez.listify.model.entity.ShoppingListState
import com.estebancoloradogonzalez.listify.utils.Queries

@Dao
interface ShoppingListStateDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(shoppingListState: ShoppingListState): Long

    @Query(Queries.SELECT_SHOPPING_LIST_STATE_BY_ID)
    suspend fun getById(id: Long): ShoppingListState?

    @Query(Queries.SELECT_SHOPPING_LIST_STATE_BY_SHOPPING_LIST)
    suspend fun getByShoppingList(shoppingList: Long): ShoppingListState?

    @Query(Queries.UPDATE_SHOPPING_LIST_STATE)
    suspend fun updateStateById(id: Long, state: Long): Int

    @Query(Queries.DELETE_SHOPPING_LIST_STATE_BY_ID)
    suspend fun deleteById(id: Long): Int
}