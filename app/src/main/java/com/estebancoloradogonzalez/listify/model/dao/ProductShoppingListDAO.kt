package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.*
import com.estebancoloradogonzalez.listify.model.dto.ProductShoppingListDetailDTO
import com.estebancoloradogonzalez.listify.model.entity.ProductShoppingList
import com.estebancoloradogonzalez.listify.utils.Queries

/**
 * Data Access Object for the ProductShoppingList entity.
 * Provides CRUD operations and custom queries for the ProductShoppingList table.
 */
@Dao
interface ProductShoppingListDAO {
    /**
     * Inserts a new ProductShoppingList into the database.
     * @param productShoppingList The ProductShoppingList entity to insert.
     * @return The row ID of the newly inserted entity, or -1 if it already exists.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(productShoppingList: ProductShoppingList): Long

    /**
     * Retrieves a ProductShoppingList by its ID.
     * @param id The ID of the ProductShoppingList.
     * @return The ProductShoppingList entity if found, null otherwise.
     */
    @Query(Queries.SELECT_PRODUCT_SHOPPING_LIST_BY_ID)
    suspend fun getById(id: Long): ProductShoppingList?

    /**
     * Retrieves all ProductShoppingList entities for a specific shopping list.
     * @param shoppingList The ID of the ShoppingList.
     * @return List of ProductShoppingList entities.
     */
    @Query(Queries.SELECT_PRODUCT_SHOPPING_LIST_BY_SHOPPING_LIST)
    suspend fun getByShoppingList(shoppingList: Long): List<ProductShoppingList>

    /**
     * Retrieves detailed information for a ProductShoppingList by its ID.
     * @param productShoppingListId The ID of the ProductShoppingList.
     * @return ProductShoppingListDetailDTO if found, null otherwise.
     */
    @Query(Queries.GET_PRODUCT_SHOPPING_LIST_DETAIL_BY_ID)
    suspend fun getDetailById(productShoppingListId: Long): ProductShoppingListDetailDTO?

    /**
     * Updates the unit price and purchased amount of a ProductShoppingList by its ID.
     * @param id The ID of the ProductShoppingList.
     * @param unitPrice The new unit price.
     * @param purchasedAmount The new purchased amount.
     * @return The number of rows updated.
     */
    @Query(Queries.UPDATE_PRODUCT_SHOPPING_LIST)
    suspend fun updateById(id: Long, unitPrice: Double, purchasedAmount: Double): Int

    /**
     * Updates the 'isReady' status of a ProductShoppingList by its ID.
     * @param productShoppingListId The ID of the ProductShoppingList.
     * @param isReady The new 'isReady' status.
     */
    @Query(Queries.UPDATE_IS_READY_BY_ID)
    suspend fun updateIsReadyById(productShoppingListId: Long, isReady: Boolean)

    /**
     * Deletes a ProductShoppingList by its ID.
     * @param id The ID of the ProductShoppingList to delete.
     * @return The number of rows deleted.
     */
    @Query(Queries.DELETE_PRODUCT_SHOPPING_LIST_BY_ID)
    suspend fun deleteById(id: Long): Int

    /**
     * Retrieves the total amount by shopping list and establishment name.
     * @param shoppingListId The ID of the shopping list.
     * @param establishmentName The name of the establishment.
     * @return The total amount, or null if not available.
     */
    @Query(Queries.GET_TOTAL_AMOUNT_BY_SHOPPING_LIST_AND_ESTABLISHMENT)
    suspend fun getTotalAmountByShoppingListAndEstablishment(
        shoppingListId: Long,
        establishmentName: String
    ): Double?
}