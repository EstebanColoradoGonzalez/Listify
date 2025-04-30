package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.estebancoloradogonzalez.listify.model.dto.ProductToAnalyzeDTO
import com.estebancoloradogonzalez.listify.model.dto.ShoppingListDTO
import com.estebancoloradogonzalez.listify.model.entity.ShoppingList
import com.estebancoloradogonzalez.listify.utils.Queries
import java.time.LocalDateTime

@Dao
interface ShoppingListDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(shoppingList: ShoppingList): Long

    @Query(Queries.SELECT_SHOPPING_LISTS)
    suspend fun getShoppingLists(user: Long): List<ShoppingListDTO>

    @Query(Queries.SELECT_PRODUCTS_TO_ANALYZE)
    suspend fun getProductsToAnalyzeDTO(shoppingList: Long): List<ProductToAnalyzeDTO>

    @Query(Queries.SELECT_MOST_RECENT_SHOPPING_LIST_DATE)
    suspend fun getMostRecentShoppingListDate(user: Long): LocalDateTime?

    @Query(Queries.SELECT_SHOPPING_LIST_BY_ID)
    suspend fun getShoppingListById(id: Long): ShoppingList?

    @Query(Queries.SELECT_LAST_SHOPPING_LIST)
    suspend fun getLastShoppingList(user: Long): ShoppingListDTO?

    @Query(Queries.UPDATE_SHOPPING_LIST)
    suspend fun updateShoppingList(id: Long, newShoppingListDate: LocalDateTime)

    @Query(Queries.DELETE_SHOPPING_LIST)
    suspend fun deleteShoppingListById(id: Long)
}