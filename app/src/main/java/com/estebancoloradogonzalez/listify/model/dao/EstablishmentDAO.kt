package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.estebancoloradogonzalez.listify.model.entity.Establishment
import com.estebancoloradogonzalez.listify.utils.Queries

@Dao
interface EstablishmentDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg establishments: Establishment)

    @Query(Queries.SELECT_ESTABLISHMENTS)
    suspend fun getEstablishments(): List<Establishment>

    @Query(Queries.SELECT_ESTABLISHMENT_BY_NAME)
    suspend fun getEstablishmentByName(name: String): Establishment?

    @Query(Queries.DELETE_ESTABLISHMENTS)
    suspend fun deleteAll()
}
