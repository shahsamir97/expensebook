package com.mdshahsamir.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mdshahsamir.database.data.Expense

@Dao
interface ExpenseDao {
    @Query("Select * from expense")
    fun getAllExpenseCategories(): List<Expense>

    @Insert
    fun addExpenseCategory(expense: Expense)

    @Update
    fun updateExpenseCategory(expense: Expense)

    @Delete
    fun deleteCategory(expense: Expense)
}