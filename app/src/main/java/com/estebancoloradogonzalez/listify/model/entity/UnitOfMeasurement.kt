package com.estebancoloradogonzalez.listify.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import com.estebancoloradogonzalez.listify.utils.NumericConstants
import com.estebancoloradogonzalez.listify.utils.TextConstants

@Entity(tableName = TextConstants.TABLE_UNIT_OF_MEASUREMENT)
data class UnitOfMeasurement(
    @PrimaryKey(autoGenerate = true)
    val id: Long = NumericConstants.ZERO,
    @ColumnInfo(name = TextConstants.COLUMN_NAME)
    val name: String,
    @ColumnInfo(name = TextConstants.COLUMN_SYMBOL)
    val symbol: String,
)
