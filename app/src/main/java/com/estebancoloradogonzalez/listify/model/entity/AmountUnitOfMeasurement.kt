package com.estebancoloradogonzalez.listify.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.estebancoloradogonzalez.listify.utils.NumericConstants
import com.estebancoloradogonzalez.listify.utils.TextConstants

@Entity(
    tableName = TextConstants.TABLE_AMOUNT_UNIT_OF_MEASUREMENT,
    foreignKeys = [
        ForeignKey(
            entity = Amount::class,
            parentColumns = [TextConstants.COLUMN_ID],
            childColumns = [TextConstants.TABLE_AMOUNT],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UnitOfMeasurement::class,
            parentColumns = [TextConstants.COLUMN_ID],
            childColumns = [TextConstants.TABLE_UNIT_OF_MEASUREMENT]
        )
    ],
    indices = [
        Index(value = [TextConstants.TABLE_AMOUNT], unique = true),
        Index(value = [TextConstants.TABLE_UNIT_OF_MEASUREMENT], unique = true)
    ]
)
data class AmountUnitOfMeasurement(
    @PrimaryKey(autoGenerate = true)
    val id: Long = NumericConstants.ZERO,
    @ColumnInfo(name = TextConstants.TABLE_AMOUNT)
    val amount: Long,
    @ColumnInfo(name = TextConstants.TABLE_UNIT_OF_MEASUREMENT)
    val unitOfMeasurement: Long,
)
