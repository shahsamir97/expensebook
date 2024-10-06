package com.mdshahsamir.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import com.mdshahsamir.database.data.Transaction

@Dao
interface TransactionDao {
    @Insert
    fun addTransaction(transaction: Transaction)

    @Delete
    fun deleteTransaction(transaction: Transaction)
}