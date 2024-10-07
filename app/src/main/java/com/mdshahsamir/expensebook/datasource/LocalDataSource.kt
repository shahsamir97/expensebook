package com.mdshahsamir.expensebook.datasource

import com.mdshahsamir.database.dao.ExpenseDao
import com.mdshahsamir.database.dao.TransactionDao
import com.mdshahsamir.database.data.Expense
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface LocalDataSource {
    suspend fun addCategory(expense: Expense)
    suspend fun updateCategory(expense: Expense)
    suspend fun getAllCategories(): Flow<List<Expense>>
    suspend fun deleteCategory(expense: Expense)
    suspend fun addTransaction(expense: Expense)
    suspend fun deleteTransaction(expense: Expense)
}

class LocalDataSourceImpl @Inject constructor(
    private val expenseDao: ExpenseDao,
    private val transactionDao: TransactionDao,
): LocalDataSource {

    override suspend fun addCategory(expense: Expense) {
        expenseDao.addExpenseCategory(expense)
    }

    override suspend fun updateCategory(expense: Expense) {

    }

    override suspend fun getAllCategories(): Flow<List<Expense>> =
        expenseDao.getAllExpenseCategories()

    override suspend fun deleteCategory(expense: Expense) {

    }

    override suspend fun addTransaction(expense: Expense) {}

    override suspend fun deleteTransaction(expense: Expense) {}
}