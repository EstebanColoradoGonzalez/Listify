package com.estebancoloradogonzalez.listify.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import com.estebancoloradogonzalez.listify.utils.TextConstants

@Entity(tableName = TextConstants.TABLE_BUDGET)
data class Budget(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = TextConstants.COLUMN_VALUE)
    val value: Double,

    @ColumnInfo(name = TextConstants.USER)
    val userId: Long
)