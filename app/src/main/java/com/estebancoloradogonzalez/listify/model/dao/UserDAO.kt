package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.estebancoloradogonzalez.listify.model.entity.User
import com.estebancoloradogonzalez.listify.utils.Queries

@Dao
interface UserDAO {
    @Query(Queries.SELECT_USER)
    suspend fun getUser(): User?

    @Query(Queries.SELECT_USER_ID)
    suspend fun getUserId(): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)
}