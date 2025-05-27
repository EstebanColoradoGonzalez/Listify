package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.*
import com.estebancoloradogonzalez.listify.model.dto.EstablishmentNameDTO
import com.estebancoloradogonzalez.listify.model.dto.ProductToAnalyzeDTO
import com.estebancoloradogonzalez.listify.model.dto.ProductShoppingListWithEstablishmentDTO
import com.estebancoloradogonzalez.listify.model.dto.ShoppingListDTO
import com.estebancoloradogonzalez.listify.model.dto.ShoppingListTotalDTO
import com.estebancoloradogonzalez.listify.model.entity.ShoppingList
import com.estebancoloradogonzalez.listify.utils.Queries
import java.time.LocalDateTime

/**
 * Data Access Object for the ShoppingList entity.
 * Provides CRUD operations and custom queries for the ShoppingList table.
 */
@Dao
interface ShoppingListDAO {
    /**
     * Inserts a new ShoppingList into the database.
     * @param shoppingList The ShoppingList entity to insert.
     * @return The row ID of the newly inserted entity, or -1 if it already exists.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(shoppingList: ShoppingList): Long

    /**
     * Retrieves all shopping lists for a user.
     * @param user The user ID.
     * @return List of ShoppingListDTOs.
     */
    @Query(Queries.SELECT_SHOPPING_LISTS)
    suspend fun getAllByUser(user: Long): List<ShoppingListDTO>

    /**
     * Retrieves a ShoppingListDTO by its ID.
     * @param shoppingList The ID of the shopping list.
     * @return The ShoppingListDTO if found, null otherwise.
     */
    @Query(Queries.SELECT_SHOPPING_LIST_DTO_BY_ID)
    suspend fun getDTOById(shoppingList: Long): ShoppingListDTO?

    /**
     * Retrieves shopping lists to analyze for a user.
     * @param user The user ID.
     * @return List of ShoppingListDTOs.
     */
    @Query(Queries.SELECT_SHOPPING_LISTS_TO_ANALYZE)
    suspend fun getToAnalyzeByUser(user: Long): List<ShoppingListDTO>

    /**
     * Retrieves products to analyze for a specific shopping list.
     * @param shoppingList The ID of the shopping list.
     * @return List of ProductToAnalyzeDTOs.
     */
    @Query(Queries.SELECT_PRODUCTS_TO_ANALYZE)
    suspend fun getProductsToAnalyzeDTO(shoppingList: Long): List<ProductToAnalyzeDTO>

    /**
     * Retrieves the most recent shopping list date for a user.
     * @param user The user ID.
     * @return The most recent shopping list date, or null if not available.
     */
    @Query(Queries.SELECT_MOST_RECENT_SHOPPING_LIST_DATE)
    suspend fun getMostRecentDateByUser(user: Long): LocalDateTime?

    /**
     * Retrieves a ShoppingList entity by its ID.
     * @param id The ID of the shopping list.
     * @return The ShoppingList entity if found, null otherwise.
     */
    @Query(Queries.SELECT_SHOPPING_LIST_BY_ID)
    suspend fun getById(id: Long): ShoppingList?

    /**
     * Retrieves the last shopping list for a user.
     * @param user The user ID.
     * @return The last ShoppingListDTO if found, null otherwise.
     */
    @Query(Queries.SELECT_LAST_SHOPPING_LIST)
    suspend fun getLastByUser(user: Long): ShoppingListDTO?

    /**
     * Updates the date of a shopping list by its ID.
     * @param id The ID of the shopping list.
     * @param newShoppingListDate The new date to set.
     */
    @Query(Queries.UPDATE_SHOPPING_LIST)
    suspend fun updateDateById(id: Long, newShoppingListDate: LocalDateTime)

    /**
     * Deletes a shopping list by its ID.
     * @param id The ID of the shopping list to delete.
     */
    @Query(Queries.DELETE_SHOPPING_LIST)
    suspend fun deleteById(id: Long)

    /**
     * Retrieves all establishments for a specific shopping list.
     * @param shoppingListId The ID of the shopping list.
     * @return List of EstablishmentNameDTOs.
     */
    @Query(Queries.GET_ESTABLISHMENTS_FOR_SHOPPING_LIST)
    suspend fun getEstablishmentsByShoppingListId(shoppingListId: Long): List<EstablishmentNameDTO>

    /**
     * Retrieves products for a shopping list and establishment.
     * @param shoppingListId The ID of the shopping list.
     * @param establishmentName The name of the establishment.
     * @return List of ProductShoppingListWithEstablishmentDTOs.
     */
    @Query(Queries.GET_PRODUCTS_BY_SHOPPING_LIST_AND_ESTABLISHMENT)
    suspend fun getProductsByShoppingListAndEstablishment(
        shoppingListId: Long,
        establishmentName: String
    ): List<ProductShoppingListWithEstablishmentDTO>

    /**
     * Retrieves the date and total amount for a shopping list.
     * @param shoppingListId The ID of the shopping list.
     * @return ShoppingListTotalDTO if found, null otherwise.
     */
    @Query(Queries.GET_SHOPPING_LIST_DATE_AND_TOTAL_AMOUNT)
    suspend fun getDateAndTotalAmountByShoppingListId(shoppingListId: Long): ShoppingListTotalDTO?
}