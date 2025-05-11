package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.estebancoloradogonzalez.listify.model.dto.EstablishmentNameDTO
import com.estebancoloradogonzalez.listify.model.dto.ProductToAnalyzeDTO
import com.estebancoloradogonzalez.listify.model.dto.ProductShoppingListWithEstablishmentDTO
import com.estebancoloradogonzalez.listify.model.dto.ShoppingListDTO
import com.estebancoloradogonzalez.listify.model.dto.ShoppingListTotalDTO
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

    @Query(Queries.GET_ESTABLISHMENTS_FOR_SHOPPING_LIST)
    suspend fun getEstablishmentsForShoppingList(shoppingListId: Long): List<EstablishmentNameDTO>

    @Query(Queries.GET_PRODUCTS_BY_SHOPPING_LIST_AND_ESTABLISHMENT)
    suspend fun getProductsByShoppingListAndEstablishment(
        shoppingListId: Long,
        establishmentName: String
    ): List<ProductShoppingListWithEstablishmentDTO>

    @Query(Queries.GET_SHOPPING_LIST_DATE_AND_TOTAL_AMOUNT)
    suspend fun getShoppingListDateAndTotalAmount(shoppingListId: Long): ShoppingListTotalDTO?
}