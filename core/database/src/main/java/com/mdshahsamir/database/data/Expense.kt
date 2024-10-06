package com.mdshahsamir.database.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Expense(
    @PrimaryKey(autoGenerate = true) val uid: Long = 0,
    @ColumnInfo(name = "category")
    val category: String,
    @ColumnInfo("budget")
    val budget: Float,
    @ColumnInfo(name = "spendAmount")
    val spend: Float,
)