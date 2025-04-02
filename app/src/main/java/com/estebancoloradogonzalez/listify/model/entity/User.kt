package com.estebancoloradogonzalez.listify.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.estebancoloradogonzalez.listify.utils.NumericConstants
import com.estebancoloradogonzalez.listify.utils.TextConstants
import java.time.LocalDateTime

@Entity(tableName = TextConstants.TABLE_USER,
    foreignKeys = [ForeignKey(
        entity = Budget::class,
        parentColumns = [TextConstants.COLUMN_ID],
        childColumns = [TextConstants.TABLE_BUDGET],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = [TextConstants.TABLE_BUDGET], unique = true)]
    )
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = NumericConstants.LONG_ZERO,
    @ColumnInfo(name = TextConstants.COLUMN_NAME)
    val name: String,
    @ColumnInfo(name = TextConstants.COLUMN_REGISTRATION_DATE)
    val registrationDate: LocalDateTime,
    @ColumnInfo(name = TextConstants.TABLE_BUDGET)
    val budget: Long
)