package com.mdshahsamir.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mdshahsamir.database.dao.ExpenseDao
import com.mdshahsamir.database.dao.TransactionDao
import com.mdshahsamir.database.data.Expense
import com.mdshahsamir.database.data.Transaction

@Database(entities = [Expense::class, Transaction::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
    abstract fun transactionDao(): TransactionDao
}