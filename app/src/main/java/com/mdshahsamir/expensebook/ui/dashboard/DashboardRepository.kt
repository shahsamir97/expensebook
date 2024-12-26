package com.mdshahsamir.expensebook.ui.dashboard

import android.content.SharedPreferences
import com.mdshahsamir.database.data.Transaction
import com.mdshahsamir.expensebook.datasource.LocalDataSource
import com.mdshahsamir.expensebook.model.Expense
import com.mdshahsamir.expensebook.model.TransactionData
import com.mdshahsamir.expensebook.model.TransactionType
import com.mdshahsamir.expensebook.toExpense
import com.mdshahsamir.expensebook.toTimestamp
import com.mdshahsamir.expensebook.toUiDateFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface DashboardRepository {
    suspend fun addCategory(expense: Expense)
    suspend fun updateCategory(expense: Expense)
    suspend fun getAllCategories(): Flow<List<Expense>>
    suspend fun deleteCategory(expense: Expense)
    suspend fun getAllTransaction(): Flow<List<TransactionData>>

    suspend fun addTransaction(
        expense: Expense,
        @TransactionType type: String,
        transactionAmount: Float,
    )

    suspend fun deleteTransaction(transactions: List<TransactionData>)

    suspend fun setIncome(amount: Float)

    suspend fun getIncomeAmount(): Float
}

class DashboardRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val sharedPreferences: SharedPreferences,
): DashboardRepository {
    override suspend fun addCategory(expense: Expense) {
        withContext(Dispatchers.IO) {
            localDataSource.addCategory(expense.toExpense())
        }
    }

    override suspend fun updateCategory(expense: Expense) {
        withContext(Dispatchers.IO) {
            localDataSource.updateCategory(expense.toExpense())
        }
    }

    override suspend fun getAllCategories(): Flow<List<Expense>> =
        localDataSource.getAllCategories().map { listOfCategories ->
            listOfCategories.map { expense ->
                Expense(
                    id = expense.uid,
                    category = expense.category,
                    budget = expense.budget,
                    spendAmount = expense.spend
                )
            }
    }

    override suspend fun deleteCategory(expense: Expense) {
        withContext(Dispatchers.IO) {
            localDataSource.deleteCategory(expense.toExpense())
        }
    }

    override suspend fun getAllTransaction(): Flow<List<TransactionData>> =
        localDataSource.getAllTransaction().map { listOfTransaction ->
            listOfTransaction.map {
                TransactionData(
                    transactionId = it.transactionId,
                    category = it.category,
                    amount = it.amount,
                    time = it.time.toUiDateFormat(),
                    type = it.type
                )
            }
        }

    override suspend fun addTransaction(
        expense: Expense,
        @TransactionType type: String,
        transactionAmount: Float,
    ) {
        withContext(Dispatchers.IO) {
            localDataSource.addTransaction(
                Transaction(
                    amount = transactionAmount,
                    category = expense.category,
                    time = System.currentTimeMillis(),
                    type = type
                )
            )
        }
    }

    override suspend fun deleteTransaction(transactions: List<TransactionData>) {
        withContext(Dispatchers.IO) {
            localDataSource.deleteTransaction(
                transactions.map {
                    Transaction(
                        transactionId = it.transactionId,
                        amount = it.amount,
                        type = it.type,
                        time = it.time.toTimestamp(),
                        category = it.category
                    )
                }
            )
        }
    }

    override suspend fun setIncome(amount: Float) {
        sharedPreferences.edit().putFloat(INCOME_KEY, amount).apply()
    }

    override suspend fun getIncomeAmount(): Float {
        return sharedPreferences.getFloat(INCOME_KEY, 0f)
    }

    companion object {
        val INCOME_KEY = "income"
    }
}