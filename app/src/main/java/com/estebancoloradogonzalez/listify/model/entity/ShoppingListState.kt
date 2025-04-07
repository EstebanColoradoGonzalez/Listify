package com.estebancoloradogonzalez.listify.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.estebancoloradogonzalez.listify.utils.NumericConstants
import com.estebancoloradogonzalez.listify.utils.TextConstants

@Entity(
    tableName = TextConstants.TABLE_SHOPPING_LIST_STATE,
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = [TextConstants.COLUMN_ID],
            childColumns = [TextConstants.TABLE_SHOPPING_LIST],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Category::class,
            parentColumns = [TextConstants.COLUMN_ID],
            childColumns = [TextConstants.TABLE_STATE]
        )
    ],
    indices = [
        Index(value = [TextConstants.TABLE_SHOPPING_LIST]),
        Index(value = [TextConstants.TABLE_STATE])
    ]
)
data class ShoppingListState(
    @PrimaryKey(autoGenerate = true)
    val id: Long = NumericConstants.LONG_ZERO,
    @ColumnInfo(name = TextConstants.TABLE_SHOPPING_LIST)
    val shoppingList: Long,
    @ColumnInfo(name = TextConstants.TABLE_STATE)
    val state: Long,
)
