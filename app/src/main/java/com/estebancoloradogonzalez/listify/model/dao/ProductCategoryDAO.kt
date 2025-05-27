package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.*
import com.estebancoloradogonzalez.listify.model.entity.ProductCategory
import com.estebancoloradogonzalez.listify.utils.Queries

/**
 * Data Access Object for the ProductCategory entity.
 * Provides CRUD operations for the ProductCategory table.
 */
@Dao
interface ProductCategoryDAO {
    /**
     * Inserts a new ProductCategory into the database.
     * @param productCategory The ProductCategory entity to insert.
     * @return The row ID of the newly inserted entity, or -1 if it already exists.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(productCategory: ProductCategory): Long

    /**
     * Retrieves a ProductCategory by its ID.
     * @param id The ID of the ProductCategory.
     * @return The ProductCategory entity if found, null otherwise.
     */
    @Query(Queries.SELECT_PRODUCT_CATEGORY_BY_ID)
    suspend fun getById(id: Long): ProductCategory?

    /**
     * Retrieves a ProductCategory by the associated product ID.
     * @param product The ID of the Product.
     * @return The ProductCategory entity if found, null otherwise.
     */
    @Query(Queries.SELECT_PRODUCT_CATEGORY_BY_PRODUCT)
    suspend fun getByProduct(product: Long): ProductCategory?

    /**
     * Updates the category of a ProductCategory by its ID.
     * @param id The ID of the ProductCategory.
     * @param category The new category ID to assign.
     * @return The number of rows updated.
     */
    @Query(Queries.UPDATE_PRODUCT_CATEGORY_CATEGORY)
    suspend fun updateCategoryById(id: Long, category: Long): Int

    /**
     * Deletes a ProductCategory by its ID.
     * @param id The ID of the ProductCategory to delete.
     * @return The number of rows deleted.
     */
    @Query(Queries.DELETE_PRODUCT_CATEGORY_BY_ID)
    suspend fun deleteById(id: Long): Int
}