package com.estebancoloradogonzalez.listify.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import com.estebancoloradogonzalez.listify.utils.NumericConstants
import com.estebancoloradogonzalez.listify.utils.TextConstants

@Entity(tableName = TextConstants.TABLE_AMOUNT)
data class Amount(
    @PrimaryKey(autoGenerate = true)
    val id: Long = NumericConstants.LONG_ZERO,
    @ColumnInfo(name = TextConstants.COLUMN_VALUE)
    val value: Double,
)
