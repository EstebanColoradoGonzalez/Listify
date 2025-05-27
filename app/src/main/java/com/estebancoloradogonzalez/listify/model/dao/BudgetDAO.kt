package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.estebancoloradogonzalez.listify.model.entity.Budget
import com.estebancoloradogonzalez.listify.utils.Queries

/**
 * Data Access Object for the Budget entity.
 * Provides CRUD operations for the Budget table.
 */
@Dao
interface BudgetDAO {
    /**
     * Inserts or replaces a Budget in the database.
     * @param budget The Budget entity to insert or replace.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budget: Budget)

    /**
     * Updates the budget value by its ID.
     * @param id The ID of the Budget to update.
     * @param newBudget The new budget value.
     */
    @Query(Queries.UPDATE_BUDGET)
    suspend fun updateById(id: Long, newBudget: Double)

    /**
     * Retrieves the ID of the stored Budget.
     * @return The ID of the Budget.
     */
    @Query(Queries.SELECT_BUDGET_ID)
    suspend fun getId(): Long

    /**
     * Retrieves the Budget entity from the database.
     * @return The Budget entity if found, null otherwise.
     */
    @Query(Queries.SELECT_BUDGET)
    suspend fun get(): Budget?
}