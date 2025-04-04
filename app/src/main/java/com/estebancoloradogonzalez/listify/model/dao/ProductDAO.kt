package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.*
import com.estebancoloradogonzalez.listify.model.entity.Product
import com.estebancoloradogonzalez.listify.utils.Queries

@Dao
interface ProductDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: Product): Long

    @Query(Queries.SELECT_PRODUCT_BY_ID)
    suspend fun getById(id: Long): Product?

    @Query(Queries.SELECT_PRODUCTS_BY_USER)
    suspend fun getByUser(user: Long): List<Product>

    @Query(Queries.SELECT_PRODUCT_BY_NAME)
    suspend fun getByName(name: String): Product?

    @Query(Queries.UPDATE_PRODUCT_FIELDS)
    suspend fun updateProductById(id: Long, name: String, unitPrice: Double, isActive: Boolean): Int

    @Query(Queries.DELETE_PRODUCT_BY_ID)
    suspend fun deleteById(id: Long): Int
}
