package com.estebancoloradogonzalez.listify.model.dao

import androidx.room.*
import com.estebancoloradogonzalez.listify.model.dto.ActiveProductWithAmountDTO
import com.estebancoloradogonzalez.listify.model.dto.ProductDTO
import com.estebancoloradogonzalez.listify.model.dto.ProductIdNameDTO
import com.estebancoloradogonzalez.listify.model.dto.ProductToAnalyzeDTO
import com.estebancoloradogonzalez.listify.model.dto.ProductToUpdateDTO
import com.estebancoloradogonzalez.listify.model.entity.Product
import com.estebancoloradogonzalez.listify.utils.Queries

@Dao
interface ProductDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(product: Product): Long

    @Query(Queries.SELECT_PRODUCT_BY_ID)
    suspend fun getById(id: Long): Product?

    @Query(Queries.SELECT_PRODUCTS_DTO)
    suspend fun getProducts(user: Long): List<ProductDTO>

    @Query(Queries.SELECT_ALL_PRODUCTS_TO_ANALYZE)
    suspend fun getProductsToAnalyzeDTO(user: Long): List<ProductToAnalyzeDTO>

    @Query(Queries.SELECT_PRODUCT_TO_UPDATE_DTO)
    suspend fun getProductToUpdate(id: Long): ProductToUpdateDTO?

    @Query(Queries.GET_PRODUCT_ID_NAME_BY_ESTABLISHMENT_NAME)
    suspend fun getProductsByEstablishmentName(establishmentName: String): List<ProductIdNameDTO>

    @Query(Queries.SELECT_PRODUCT_BY_NAME)
    suspend fun getByName(name: String): Product?

    @Query(Queries.SELECT_PRODUCT_BY_NAME_TO_UPDATE)
    suspend fun getByNameToUpdate(name: String, productId: Long): Product?

    @Query(Queries.UPDATE_PRODUCT_FIELDS)
    suspend fun updateProductById(id: Long, name: String, unitPrice: Double, isActive: Boolean): Int

    @Query(Queries.UPDATE_UNIT_PRICE_BY_PRODUCT_ID)
    suspend fun updateUnitPrice(productId: Long, newUnitPrice: Double)

    @Query(Queries.DELETE_PRODUCT_BY_ID)
    suspend fun deleteById(id: Long): Int

    @Query(Queries.SELECT_TOTAL_EXPENDITURE)
    suspend fun getTotalExpenditure(): Double?

    @Query(Queries.SELECT_USER_EXCEED_BUDGET_TO_CREATE)
    suspend fun isTotalExpenditureExceedBudgetToCreate(userId: Long, additionalCost: Double): Boolean

    @Query(Queries.SELECT_USER_EXCEED_BUDGET_TO_UPDATE)
    suspend fun isTotalExpenditureExceedBudgetToUpdate(userId: Long, additionalCost: Double, productId: Long): Boolean

    @Query(Queries.GET_ACTIVE_PRODUCTS_WITH_AMOUNT_BY_USER)
    suspend fun getActiveProductsWithAmountByUser(userId: Long): List<ActiveProductWithAmountDTO>
}
