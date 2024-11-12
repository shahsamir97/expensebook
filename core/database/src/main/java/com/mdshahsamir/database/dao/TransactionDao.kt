package com.mdshahsamir.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mdshahsamir.database.data.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("select * from 'Transaction' ORDER BY time DESC")
    fun getAllTransaction(): Flow<List<Transaction>>

    @Insert
    fun addTransaction(transaction: Transaction)

    @Query("DELETE FROM 'transaction' WHERE transactionId = :transactionId")
    fun deleteTransaction(transactionId: Long)
}