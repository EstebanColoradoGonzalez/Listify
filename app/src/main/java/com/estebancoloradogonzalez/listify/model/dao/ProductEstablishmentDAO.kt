package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.*
import com.estebancoloradogonzalez.listify.model.entity.ProductEstablishment
import com.estebancoloradogonzalez.listify.utils.Queries

/**
 * Data Access Object for the ProductEstablishment entity.
 * Provides CRUD operations for the ProductEstablishment table.
 */
@Dao
interface ProductEstablishmentDAO {
    /**
     * Inserts a new ProductEstablishment into the database.
     * @param productEstablishment The ProductEstablishment entity to insert.
     * @return The row ID of the newly inserted entity, or -1 if it already exists.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(productEstablishment: ProductEstablishment): Long

    /**
     * Retrieves a ProductEstablishment by its ID.
     * @param id The ID of the ProductEstablishment.
     * @return The ProductEstablishment entity if found, null otherwise.
     */
    @Query(Queries.SELECT_PRODUCT_ESTABLISHMENT_BY_ID)
    suspend fun getById(id: Long): ProductEstablishment?

    /**
     * Retrieves a ProductEstablishment by the associated product ID.
     * @param product The ID of the Product.
     * @return The ProductEstablishment entity if found, null otherwise.
     */
    @Query(Queries.SELECT_PRODUCT_ESTABLISHMENT_BY_PRODUCT)
    suspend fun getByProduct(product: Long): ProductEstablishment?

    /**
     * Updates the establishment of a ProductEstablishment by its ID.
     * @param id The ID of the ProductEstablishment.
     * @param establishment The new establishment ID to assign.
     * @return The number of rows updated.
     */
    @Query(Queries.UPDATE_PRODUCT_ESTABLISHMENT_ESTABLISHMENT)
    suspend fun updateEstablishmentById(id: Long, establishment: Long): Int

    /**
     * Deletes a ProductEstablishment by its ID.
     * @param id The ID of the ProductEstablishment to delete.
     * @return The number of rows deleted.
     */
    @Query(Queries.DELETE_PRODUCT_ESTABLISHMENT_BY_ID)
    suspend fun deleteById(id: Long): Int
}