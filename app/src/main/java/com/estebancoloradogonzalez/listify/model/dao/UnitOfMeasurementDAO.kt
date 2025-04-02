package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.estebancoloradogonzalez.listify.model.entity.UnitOfMeasurement
import com.estebancoloradogonzalez.listify.utils.Queries

@Dao
interface UnitOfMeasurementDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg units: UnitOfMeasurement)

    @Query(Queries.SELECT_UNIT_OF_MEASUREMENT)
    suspend fun getUnitsOfMeasurement(): List<UnitOfMeasurement>

    @Query(Queries.DELETE_UNITS_OF_MEASUREMENT)
    suspend fun deleteAll()
}
