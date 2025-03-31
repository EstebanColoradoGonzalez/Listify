package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.estebancoloradogonzalez.listify.model.entity.Budget
import com.estebancoloradogonzalez.listify.utils.Queries

@Dao
interface BudgetDAO {
    @Insert
    suspend fun insertBudget(budget: Budget)

    @Query(Queries.UPDATE_BUDGET)
    suspend fun updateBudget(id: Long, newBudget: Double)

    @Query(Queries.SELECT_BUDGET_ID)
    suspend fun getBudgetId(): Long

    @Query(Queries.SELECT_BUDGET)
    suspend fun getBudget(): Budget?
}