package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.*
import com.estebancoloradogonzalez.listify.model.entity.ProductPurchaseFrequency
import com.estebancoloradogonzalez.listify.utils.Queries

/**
 * Data Access Object for the ProductPurchaseFrequency entity.
 * Provides CRUD operations for the ProductPurchaseFrequency table.
 */
@Dao
interface ProductPurchaseFrenquencyDAO {
    /**
     * Inserts a new ProductPurchaseFrequency into the database.
     * @param productPurchaseFrequency The ProductPurchaseFrequency entity to insert.
     * @return The row ID of the newly inserted entity, or -1 if it already exists.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(productPurchaseFrequency: ProductPurchaseFrequency): Long

    /**
     * Retrieves a ProductPurchaseFrequency by its ID.
     * @param id The ID of the ProductPurchaseFrequency.
     * @return The ProductPurchaseFrequency entity if found, null otherwise.
     */
    @Query(Queries.SELECT_PRODUCT_PURCHASE_FREQUENCY_BY_ID)
    suspend fun getById(id: Long): ProductPurchaseFrequency?

    /**
     * Retrieves a ProductPurchaseFrequency by the associated product ID.
     * @param product The ID of the Product.
     * @return The ProductPurchaseFrequency entity if found, null otherwise.
     */
    @Query(Queries.SELECT_PRODUCT_PURCHASE_FREQUENCY_BY_PRODUCT)
    suspend fun getByProduct(product: Long): ProductPurchaseFrequency?

    /**
     * Updates the purchase frequency of a ProductPurchaseFrequency by its ID.
     * @param id The ID of the ProductPurchaseFrequency.
     * @param purchaseFrequency The new purchase frequency value.
     * @return The number of rows updated.
     */
    @Query(Queries.UPDATE_PRODUCT_PURCHASE_FREQUENCY_FIELD)
    suspend fun updatePurchaseFrequencyById(id: Long, purchaseFrequency: Long): Int

    /**
     * Deletes a ProductPurchaseFrequency by its ID.
     * @param id The ID of the ProductPurchaseFrequency to delete.
     * @return The number of rows deleted.
     */
    @Query(Queries.DELETE_PRODUCT_PURCHASE_FREQUENCY_BY_ID)
    suspend fun deleteById(id: Long): Int
}