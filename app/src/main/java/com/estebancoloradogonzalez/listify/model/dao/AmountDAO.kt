package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.*
import com.estebancoloradogonzalez.listify.model.entity.Amount
import com.estebancoloradogonzalez.listify.utils.Queries

@Dao
interface AmountDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(amount: Amount): Long

    @Query(Queries.SELECT_AMOUNT_BY_ID)
    suspend fun getAmountById(id: Long): Amount?

    @Query(Queries.UPDATE_AMOUNT)
    suspend fun updateValueById(id: Long, value: Double): Int

    @Query(Queries.DELETE_AMOUNT)
    suspend fun deleteById(id: Long): Int
}
