package com.mdshahsamir.expensebook.datasource

import com.mdshahsamir.database.dao.ExpenseDao
import com.mdshahsamir.database.dao.TransactionDao
import com.mdshahsamir.database.data.ExpenseDbModel
import com.mdshahsamir.database.data.Transaction
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface LocalDataSource {
    suspend fun addCategory(expenseDbModel: ExpenseDbModel)
    suspend fun updateCategory(expenseDbModel: ExpenseDbModel)
    suspend fun getAllCategories(): Flow<List<ExpenseDbModel>>
    suspend fun deleteCategory(expenseDbModel: ExpenseDbModel)
    suspend fun getAllTransaction(): Flow<List<Transaction>>
    suspend fun addTransaction(transaction: Transaction)
    suspend fun deleteTransaction(transactions: List<Transaction>)
}

class LocalDataSourceImpl @Inject constructor(
    private val expenseDao: ExpenseDao,
    private val transactionDao: TransactionDao,
): LocalDataSource {

    override suspend fun addCategory(expenseDbModel: ExpenseDbModel) {
        expenseDao.addExpenseCategory(expenseDbModel)
    }

    override suspend fun updateCategory(expenseDbModel: ExpenseDbModel) {
        expenseDao.updateExpenseCategory(expenseDbModel)
    }

    override suspend fun getAllCategories(): Flow<List<ExpenseDbModel>> =
        expenseDao.getAllExpenseCategories()

    override suspend fun deleteCategory(expenseDbModel: ExpenseDbModel) =
        expenseDao.deleteCategory(expenseDbModel)

    override suspend fun getAllTransaction(): Flow<List<Transaction>> =
        transactionDao.getAllTransaction()

    override suspend fun addTransaction(transaction: Transaction) =
        transactionDao.addTransaction(transaction)

    override suspend fun deleteTransaction(transactions: List<Transaction>) =
        transactionDao.deleteTransaction(transactions)
}