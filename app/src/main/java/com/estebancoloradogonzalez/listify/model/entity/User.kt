package com.estebancoloradogonzalez.listify.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.estebancoloradogonzalez.listify.utils.NumericConstants
import com.estebancoloradogonzalez.listify.utils.TextConstants
import java.time.LocalDateTime

@Entity(tableName = TextConstants.USER)
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = NumericConstants.ZERO,
    val name: String,
    val registrationDate: LocalDateTime
)