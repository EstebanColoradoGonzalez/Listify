package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.estebancoloradogonzalez.listify.model.entity.State

@Dao
interface StateDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg states: State)

    @Query("DELETE FROM state")
    suspend fun deleteAll()
}
