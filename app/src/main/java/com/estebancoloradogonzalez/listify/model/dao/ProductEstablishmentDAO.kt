package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.*
import com.estebancoloradogonzalez.listify.model.entity.ProductEstablishment
import com.estebancoloradogonzalez.listify.utils.Queries

@Dao
interface ProductEstablishmentDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(productEstablishment: ProductEstablishment): Long

    @Query(Queries.SELECT_PRODUCT_ESTABLISHMENT_BY_ID)
    suspend fun getById(id: Long): ProductEstablishment?

    @Query(Queries.SELECT_PRODUCT_ESTABLISHMENT_BY_PRODUCT)
    suspend fun getByProduct(product: Long): ProductEstablishment?

    @Query(Queries.UPDATE_PRODUCT_ESTABLISHMENT_ESTABLISHMENT)
    suspend fun updateEstablishmentById(id: Long, establishment: Long): Int

    @Query(Queries.DELETE_PRODUCT_ESTABLISHMENT_BY_ID)
    suspend fun deleteById(id: Long): Int
}