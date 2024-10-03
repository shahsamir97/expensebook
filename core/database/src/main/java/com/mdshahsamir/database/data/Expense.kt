package com.mdshahsamir.database.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Expense(
    @PrimaryKey val uid:Int,
    @ColumnInfo(name = "category")
    val category: String,
    @ColumnInfo("budget")
    val budget: Float,
    @ColumnInfo(name = "spendAmount")
    val spend: Float,
)