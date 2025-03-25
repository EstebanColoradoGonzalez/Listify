package com.estebancoloradogonzalez.listify.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import com.estebancoloradogonzalez.listify.utils.NumericConstants
import com.estebancoloradogonzalez.listify.utils.TextConstants

@Entity(tableName = TextConstants.TABLE_PURCHASE_FREQUENCY)
data class PurchaseFrequency(
    @PrimaryKey(autoGenerate = true)
    val id: Long = NumericConstants.ZERO,
    @ColumnInfo(name = TextConstants.COLUMN_NAME)
    val name: String,
)