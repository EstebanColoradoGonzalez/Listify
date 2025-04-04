package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.*
import com.estebancoloradogonzalez.listify.model.entity.ProductPurchaseFrequency
import com.estebancoloradogonzalez.listify.utils.Queries

@Dao
interface ProductPurchaseFrenquencyDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(productPurchaseFrequency: ProductPurchaseFrequency): Long

    @Query(Queries.SELECT_PRODUCT_PURCHASE_FREQUENCY_BY_ID)
    suspend fun getById(id: Long): ProductPurchaseFrequency?

    @Query(Queries.SELECT_PRODUCT_PURCHASE_FREQUENCY_BY_PRODUCT)
    suspend fun getByProduct(product: Long): List<ProductPurchaseFrequency>

    @Query(Queries.UPDATE_PRODUCT_PURCHASE_FREQUENCY_FIELD)
    suspend fun updatePurchaseFrequencyById(id: Long, purchaseFrequency: Long): Int

    @Query(Queries.DELETE_PRODUCT_PURCHASE_FREQUENCY_BY_ID)
    suspend fun deleteById(id: Long): Int
}
