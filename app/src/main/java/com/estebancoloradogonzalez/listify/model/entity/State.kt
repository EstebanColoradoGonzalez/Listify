package com.estebancoloradogonzalez.listify.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import com.estebancoloradogonzalez.listify.utils.NumericConstants
import com.estebancoloradogonzalez.listify.utils.TextConstants

@Entity(tableName = TextConstants.TABLE_STATE)
data class State(
    @PrimaryKey(autoGenerate = true)
    val id: Long = NumericConstants.ZERO,
    @ColumnInfo(name = TextConstants.COLUMN_NAME)
    val name: String,
)