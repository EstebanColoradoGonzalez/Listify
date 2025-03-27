package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.estebancoloradogonzalez.listify.model.entity.PurchaseFrequency

@Dao
interface PurchaseFrequencyDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg frequencies: PurchaseFrequency)

    @Query("DELETE FROM purchase_frequency")
    suspend fun deleteAll()
}
