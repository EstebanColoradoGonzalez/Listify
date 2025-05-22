package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.estebancoloradogonzalez.listify.model.entity.State
import com.estebancoloradogonzalez.listify.utils.Queries

@Dao
interface StateDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg states: State)

    @Query(Queries.SELECT_STATE_BY_NAME)
    suspend fun getStateByName(name: String): State?

    @Query(Queries.DELETE_STATES)
    suspend fun deleteAll()
}
