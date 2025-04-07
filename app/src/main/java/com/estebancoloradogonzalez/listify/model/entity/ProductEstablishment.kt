package com.estebancoloradogonzalez.listify.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.estebancoloradogonzalez.listify.utils.NumericConstants
import com.estebancoloradogonzalez.listify.utils.TextConstants

@Entity(
    tableName = TextConstants.TABLE_PRODUCT_ESTABLISHMENT,
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = [TextConstants.COLUMN_ID],
            childColumns = [TextConstants.TABLE_PRODUCT],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Establishment::class,
            parentColumns = [TextConstants.COLUMN_ID],
            childColumns = [TextConstants.TABLE_ESTABLISHMENT]
        )
    ],
    indices = [
        Index(value = [TextConstants.TABLE_PRODUCT]),
        Index(value = [TextConstants.TABLE_ESTABLISHMENT])
    ]
)
data class ProductEstablishment(
    @PrimaryKey(autoGenerate = true)
    val id: Long = NumericConstants.LONG_ZERO,
    @ColumnInfo(name = TextConstants.TABLE_PRODUCT)
    val product: Long,
    @ColumnInfo(name = TextConstants.TABLE_ESTABLISHMENT)
    val establishment: Long,
)