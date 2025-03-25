package com.estebancoloradogonzalez.listify.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.estebancoloradogonzalez.listify.utils.NumericConstants
import com.estebancoloradogonzalez.listify.utils.TextConstants

@Entity(tableName = TextConstants.TABLE_PRODUCT,
    foreignKeys = [ForeignKey(
        entity = Amount::class,
        parentColumns = [TextConstants.COLUMN_ID],
        childColumns = [TextConstants.TABLE_AMOUNT],
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = User::class,
        parentColumns = [TextConstants.COLUMN_ID],
        childColumns = [TextConstants.TABLE_USER],
    )],
    indices = [
        Index(value = [TextConstants.TABLE_AMOUNT], unique = true),
        Index(value = [TextConstants.TABLE_USER])
    ]
)
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long = NumericConstants.ZERO,
    @ColumnInfo(name = TextConstants.COLUMN_NAME)
    val name: String,
    @ColumnInfo(name = TextConstants.COLUMN_UNIT_PRICE)
    val unitPrice: Double,
    @ColumnInfo(name = TextConstants.COLUMN_IS_ACTIVE)
    val isActive: Boolean,
    @ColumnInfo(name = TextConstants.TABLE_AMOUNT)
    val amount: Long,
    @ColumnInfo(name = TextConstants.TABLE_USER)
    val user: Long
)