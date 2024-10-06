package com.mdshahsamir.expensebook.ui.dashboard

import com.mdshahsamir.database.dao.ExpenseDao
import com.mdshahsamir.database.dao.TransactionDao
import com.mdshahsamir.database.data.Expense
import com.mdshahsamir.database.data.Transaction
import com.mdshahsamir.expensebook.model.ExpenseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface DashboardRepository {
    suspend fun addCategory(expenseState: ExpenseState)
    suspend fun updateCategory(expenseState: ExpenseState)
    suspend fun deleteCategory(expenseState: ExpenseState)
    suspend fun addTransaction(expenseState: ExpenseState)
    suspend fun deleteTransaction(expenseState: ExpenseState)
}

class DashboardRepositoryImpl @Inject constructor(
    private val expenseDao: ExpenseDao,
    private val transactionDao: TransactionDao,
): DashboardRepository {
    override suspend fun addCategory(expenseState: ExpenseState) {
        withContext(Dispatchers.IO) {
            expenseDao.addExpenseCategory(
                Expense(
                    category = expenseState.category,
                    spend = expenseState.spendAmount,
                    budget = expenseState.budget
                )
            )
        }
    }

    override suspend fun updateCategory(expenseState: ExpenseState) {

    }

    override suspend fun deleteCategory(expenseState: ExpenseState) {

    }

    override suspend fun addTransaction(expenseState: ExpenseState) {

    }

    override suspend fun deleteTransaction(expenseState: ExpenseState) {

    }

}