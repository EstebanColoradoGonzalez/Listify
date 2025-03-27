package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.estebancoloradogonzalez.listify.model.entity.Establishment

@Dao
interface EstablishmentDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg establishments: Establishment)

    @Query("DELETE FROM establishment")
    suspend fun deleteAll()
}
