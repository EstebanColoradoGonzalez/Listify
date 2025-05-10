package com.estebancoloradogonzalez.listify.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.estebancoloradogonzalez.listify.utils.NumericConstants
import com.estebancoloradogonzalez.listify.utils.TextConstants

@Entity(tableName = TextConstants.TABLE_PRODUCT_SHOPPING_LIST,
    foreignKeys = [ForeignKey(
        entity = ShoppingList::class,
        parentColumns = [TextConstants.COLUMN_ID],
        childColumns = [TextConstants.TABLE_SHOPPING_LIST],
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Product::class,
        parentColumns = [TextConstants.COLUMN_ID],
        childColumns = [TextConstants.TABLE_PRODUCT],
    )],
    indices = [
        Index(value = [TextConstants.TABLE_SHOPPING_LIST]),
        Index(value = [TextConstants.TABLE_PRODUCT])
    ]
)
data class ProductShoppingList(
    @PrimaryKey(autoGenerate = true)
    val id: Long = NumericConstants.LONG_ZERO,
    @ColumnInfo(name = TextConstants.COLUMN_UNIT_PRICE)
    val unitPrice: Double,
    @ColumnInfo(name = TextConstants.COLUMN_PURCHASED_AMOUNT)
    val purchasedAmount: Double,
    @ColumnInfo(name = TextConstants.COLUMN_IS_READY)
    val isReady: Boolean,
    @ColumnInfo(name = TextConstants.TABLE_SHOPPING_LIST)
    val shoppingList: Long,
    @ColumnInfo(name = TextConstants.TABLE_PRODUCT)
    val product: Long
)
