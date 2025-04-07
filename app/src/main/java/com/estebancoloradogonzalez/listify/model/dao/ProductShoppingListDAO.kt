package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.estebancoloradogonzalez.listify.model.entity.ProductShoppingList
import com.estebancoloradogonzalez.listify.utils.Queries

@Dao
interface ProductShoppingListDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(productShoppingList: ProductShoppingList): Long

    @Query(Queries.SELECT_PRODUCT_SHOPPING_LIST_BY_ID)
    suspend fun getById(id: Long): ProductShoppingList?

    @Query(Queries.SELECT_PRODUCT_SHOPPING_LIST_BY_SHOPPING_LIST)
    suspend fun getByShoppingList(shoppingList: Long): List<ProductShoppingList>

    @Query(Queries.UPDATE_PRODUCT_SHOPPING_LIST)
    suspend fun updateById(id: Long, unitPrice: Double, purchasedAmount: Double, isReady: Boolean): Int

    @Query(Queries.DELETE_PRODUCT_SHOPPING_LIST_BY_ID)
    suspend fun deleteById(id: Long): Int
}