package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.estebancoloradogonzalez.listify.model.entity.UnitOfMeasurement

@Dao
interface UnitOfMeasurementDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg units: UnitOfMeasurement)

    @Query("DELETE FROM unit_of_measurement")
    suspend fun deleteAll()
}
