package com.mdshahsamir.expensebook.ui.dashboard

import com.mdshahsamir.database.data.Expense
import com.mdshahsamir.expensebook.datasource.LocalDataSource
import com.mdshahsamir.expensebook.model.ExpenseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface DashboardRepository {
    suspend fun addCategory(expenseState: ExpenseState)
    suspend fun updateCategory(expenseState: ExpenseState)
    suspend fun getAllCategories(): Flow<List<ExpenseState>>
    suspend fun deleteCategory(expenseState: ExpenseState)
    suspend fun addTransaction(expenseState: ExpenseState)
    suspend fun deleteTransaction(expenseState: ExpenseState)
}

class DashboardRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
): DashboardRepository {
    override suspend fun addCategory(expenseState: ExpenseState) {
        withContext(Dispatchers.IO) {
            localDataSource.addCategory(
                Expense(
                    category = expenseState.category,
                    budget = expenseState.budget,
                    spend = expenseState.spendAmount
                )
            )
        }
    }

    override suspend fun updateCategory(expenseState: ExpenseState) {

    }

    override suspend fun getAllCategories(): Flow<List<ExpenseState>> = localDataSource.getAllCategories().map { listOfCategories ->
        listOfCategories.map { expense ->
            ExpenseState(
                id = expense.uid,
                category = expense.category,
                budget = expense.budget,
                spendAmount = expense.spend
            )
        }
    }

    override suspend fun deleteCategory(expenseState: ExpenseState) {

    }

    override suspend fun addTransaction(expenseState: ExpenseState) {

    }

    override suspend fun deleteTransaction(expenseState: ExpenseState) {

    }

}