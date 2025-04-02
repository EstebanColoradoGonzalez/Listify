package com.estebancoloradogonzalez.listify.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.estebancoloradogonzalez.listify.utils.NumericConstants
import com.estebancoloradogonzalez.listify.utils.TextConstants

@Entity(
    tableName = TextConstants.TABLE_PRODUCT_PURCHASE_FREQUENCY,
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = [TextConstants.COLUMN_ID],
            childColumns = [TextConstants.TABLE_PRODUCT],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PurchaseFrequency::class,
            parentColumns = [TextConstants.COLUMN_ID],
            childColumns = [TextConstants.TABLE_PURCHASE_FREQUENCY]
        )
    ],
    indices = [
        Index(value = [TextConstants.TABLE_PRODUCT], unique = true),
        Index(value = [TextConstants.TABLE_PURCHASE_FREQUENCY], unique = true)
    ]
)
data class ProductPurchaseFrequency(
    @PrimaryKey(autoGenerate = true)
    val id: Long = NumericConstants.LONG_ZERO,
    @ColumnInfo(name = TextConstants.TABLE_PRODUCT)
    val product: Long,
    @ColumnInfo(name = TextConstants.TABLE_PURCHASE_FREQUENCY)
    val purchaseFrequency: Long,
)
