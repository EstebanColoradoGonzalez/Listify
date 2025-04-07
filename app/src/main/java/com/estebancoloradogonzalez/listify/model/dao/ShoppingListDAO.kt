package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.estebancoloradogonzalez.listify.model.entity.ShoppingList
import com.estebancoloradogonzalez.listify.utils.Queries
import java.time.LocalDate

@Dao
interface ShoppingListDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(shoppingList: ShoppingList)

    @Query(Queries.SELECT_SHOPPING_LISTS)
    suspend fun getShoppingLists(): List<ShoppingList>

    @Query(Queries.SELECT_SHOPPING_LIST_BY_ID)
    suspend fun getShoppingListById(id: Long): ShoppingList?

    @Query(Queries.UPDATE_SHOPPING_LIST)
    suspend fun updateShoppingList(id: Long, newDate: LocalDate)

    @Query(Queries.DELETE_SHOPPING_LIST)
    suspend fun deleteShoppingListById(id: Long)
}