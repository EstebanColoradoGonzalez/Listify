package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.estebancoloradogonzalez.listify.model.entity.PurchaseFrequency
import com.estebancoloradogonzalez.listify.utils.Queries

@Dao
interface PurchaseFrequencyDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg frequencies: PurchaseFrequency)

    @Query(Queries.SELECT_PURCHASE_FREQUENCY)
    suspend fun getPurchaseFrequencies(): List<PurchaseFrequency>

    @Query(Queries.DELETE_PURCHASE_FREQUENCIES)
    suspend fun deleteAll()
}
