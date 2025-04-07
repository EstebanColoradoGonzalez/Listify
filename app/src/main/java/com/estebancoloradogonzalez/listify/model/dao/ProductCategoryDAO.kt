package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.*
import com.estebancoloradogonzalez.listify.model.entity.ProductCategory
import com.estebancoloradogonzalez.listify.utils.Queries

@Dao
interface ProductCategoryDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(productCategory: ProductCategory): Long

    @Query(Queries.SELECT_PRODUCT_CATEGORY_BY_ID)
    suspend fun getById(id: Long): ProductCategory?

    @Query(Queries.SELECT_PRODUCT_CATEGORY_BY_PRODUCT)
    suspend fun getByProduct(product: Long): ProductCategory?

    @Query(Queries.UPDATE_PRODUCT_CATEGORY_CATEGORY)
    suspend fun updateCategoryById(id: Long, category: Long): Int

    @Query(Queries.DELETE_PRODUCT_CATEGORY_BY_ID)
    suspend fun deleteById(id: Long): Int
}