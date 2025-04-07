package com.estebancoloradogonzalez.listify.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.estebancoloradogonzalez.listify.utils.NumericConstants
import com.estebancoloradogonzalez.listify.utils.TextConstants
import java.time.LocalDateTime

@Entity(tableName = TextConstants.TABLE_SHOPPING_LIST,
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = [TextConstants.COLUMN_ID],
        childColumns = [TextConstants.TABLE_USER],
    )],
    indices = [Index(value = [TextConstants.TABLE_USER])]
)
data class ShoppingList(
    @PrimaryKey(autoGenerate = true)
    val id: Long = NumericConstants.LONG_ZERO,
    @ColumnInfo(name = TextConstants.COLUMN_SHOPPING_LIST_DATE)
    val shoppingListDate: LocalDateTime,
    @ColumnInfo(name = TextConstants.TABLE_USER)
    val user: Long
)
