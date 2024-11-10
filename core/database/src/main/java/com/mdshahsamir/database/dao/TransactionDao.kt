package com.mdshahsamir.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mdshahsamir.database.data.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("select * from 'Transaction'")
    fun getAllTransaction(): Flow<List<Transaction>>

    @Insert
    fun addTransaction(transaction: Transaction)

    @Delete
    fun deleteTransaction(transaction: Transaction)
}