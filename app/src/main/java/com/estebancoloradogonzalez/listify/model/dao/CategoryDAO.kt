package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.estebancoloradogonzalez.listify.model.entity.Category
import com.estebancoloradogonzalez.listify.utils.Queries

@Dao
interface CategoryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category)

    @Query(Queries.SELECT_CATEGORIES)
    suspend fun getCategories(): List<Category>

    @Query(Queries.SELECT_CATEGORY_BY_ID)
    suspend fun getCategoryById(id: Long): Category?

    @Query(Queries.SELECT_CATEGORY_BY_NAME)
    suspend fun getCategoryByName(name: String): Category?

    @Query(Queries.UPDATE_CATEGORY)
    suspend fun updateCategory(id: Long, newName: String)

    @Query(Queries.DELETE_CATEGORY)
    suspend fun deleteCategoryById(id: Long)
}