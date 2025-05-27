package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.*
import com.estebancoloradogonzalez.listify.model.dto.ActiveProductWithAmountDTO
import com.estebancoloradogonzalez.listify.model.dto.ProductDTO
import com.estebancoloradogonzalez.listify.model.dto.ProductIdNameDTO
import com.estebancoloradogonzalez.listify.model.dto.ProductToAnalyzeDTO
import com.estebancoloradogonzalez.listify.model.dto.ProductToUpdateDTO
import com.estebancoloradogonzalez.listify.model.entity.Product
import com.estebancoloradogonzalez.listify.utils.Queries

/**
 * Data Access Object for the Product entity.
 * Provides CRUD operations and custom queries for the Product table.
 */
@Dao
interface ProductDAO {
    /**
     * Inserts a new Product into the database.
     * @param product The Product entity to insert.
     * @return The row ID of the newly inserted entity, or -1 if it already exists.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(product: Product): Long

    /**
     * Retrieves a Product by its ID.
     * @param id The ID of the Product.
     * @return The Product entity if found, null otherwise.
     */
    @Query(Queries.SELECT_PRODUCT_BY_ID)
    suspend fun getById(id: Long): Product?

    /**
     * Retrieves a Product by its name.
     * @param name The name of the Product.
     * @return The Product entity if found, null otherwise.
     */
    @Query(Queries.SELECT_PRODUCT_BY_NAME)
    suspend fun getByName(name: String): Product?

    /**
     * Retrieves a Product by name excluding a specific product ID (for updates).
     * @param name The name of the Product.
     * @param productId The ID of the Product to exclude.
     * @return The Product entity if found, null otherwise.
     */
    @Query(Queries.SELECT_PRODUCT_BY_NAME_TO_UPDATE)
    suspend fun getByNameToUpdate(name: String, productId: Long): Product?

    /**
     * Retrieves a list of ProductDTOs for a user.
     * @param user The user ID.
     * @return List of ProductDTOs.
     */
    @Query(Queries.SELECT_PRODUCTS_DTO)
    suspend fun getProducts(user: Long): List<ProductDTO>

    /**
     * Retrieves a list of ProductToAnalyzeDTOs for a user.
     * @param user The user ID.
     * @return List of ProductToAnalyzeDTOs.
     */
    @Query(Queries.SELECT_ALL_PRODUCTS_TO_ANALYZE)
    suspend fun getProductsToAnalyzeDTO(user: Long): List<ProductToAnalyzeDTO>

    /**
     * Retrieves a ProductToUpdateDTO by product ID.
     * @param id The product ID.
     * @return ProductToUpdateDTO if found, null otherwise.
     */
    @Query(Queries.SELECT_PRODUCT_TO_UPDATE_DTO)
    suspend fun getProductToUpdate(id: Long): ProductToUpdateDTO?

    /**
     * Retrieves a list of ProductIdNameDTO by establishment name.
     * @param establishmentName The name of the establishment.
     * @return List of ProductIdNameDTOs.
     */
    @Query(Queries.GET_PRODUCT_ID_NAME_BY_ESTABLISHMENT_NAME)
    suspend fun getProductsByEstablishmentName(establishmentName: String): List<ProductIdNameDTO>

    /**
     * Updates multiple fields of a Product by its ID.
     * @param id The ID of the Product.
     * @param name The new name.
     * @param unitPrice The new unit price.
     * @param isActive The new active status.
     * @return The number of rows updated.
     */
    @Query(Queries.UPDATE_PRODUCT_FIELDS)
    suspend fun updateProductById(id: Long, name: String, unitPrice: Double, isActive: Boolean): Int

    /**
     * Updates the unit price of a Product by its ID.
     * @param productId The ID of the Product.
     * @param newUnitPrice The new unit price.
     */
    @Query(Queries.UPDATE_UNIT_PRICE_BY_PRODUCT_ID)
    suspend fun updateUnitPrice(productId: Long, newUnitPrice: Double)

    /**
     * Deletes a Product by its ID.
     * @param id The ID of the Product to delete.
     * @return The number of rows deleted.
     */
    @Query(Queries.DELETE_PRODUCT_BY_ID)
    suspend fun deleteById(id: Long): Int

    /**
     * Retrieves the total expenditure.
     * @return The total expenditure, or null if not available.
     */
    @Query(Queries.SELECT_TOTAL_EXPENDITURE)
    suspend fun getTotalExpenditure(): Double?

    /**
     * Checks if the total expenditure will exceed the budget when creating a new product.
     * @param userId The user ID.
     * @param additionalCost The additional cost to add.
     * @return True if the expenditure will exceed the budget, false otherwise.
     */
    @Query(Queries.SELECT_USER_EXCEED_BUDGET_TO_CREATE)
    suspend fun isTotalExpenditureExceedBudgetToCreate(userId: Long, additionalCost: Double): Boolean

    /**
     * Checks if the total expenditure will exceed the budget when updating a product.
     * @param userId The user ID.
     * @param additionalCost The additional cost to add.
     * @param productId The product ID being updated.
     * @return True if the expenditure will exceed the budget, false otherwise.
     */
    @Query(Queries.SELECT_USER_EXCEED_BUDGET_TO_UPDATE)
    suspend fun isTotalExpenditureExceedBudgetToUpdate(userId: Long, additionalCost: Double, productId: Long): Boolean

    /**
     * Retrieves all active products with amount for a user.
     * @param userId The user ID.
     * @return List of ActiveProductWithAmountDTOs.
     */
    @Query(Queries.GET_ACTIVE_PRODUCTS_WITH_AMOUNT_BY_USER)
    suspend fun getActiveProductsWithAmountByUser(userId: Long): List<ActiveProductWithAmountDTO>
}