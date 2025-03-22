package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.Dao
import androidx.room.Insert

import com.estebancoloradogonzalez.listify.model.entity.Budget

@Dao
interface BudgetDAO {
    @Insert
    suspend fun insertBudget(budget: Budget)
}