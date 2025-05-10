package com.mdshahsamir.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mdshahsamir.database.converter.DateConverter
import com.mdshahsamir.database.dao.ExpenseDao
import com.mdshahsamir.database.dao.TransactionDao
import com.mdshahsamir.database.data.ExpenseDbModel
import com.mdshahsamir.database.data.Transaction

@Database(entities = [ExpenseDbModel::class, Transaction::class], version = 2, exportSchema = true)
@TypeConverters(DateConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
    abstract fun transactionDao(): TransactionDao
}