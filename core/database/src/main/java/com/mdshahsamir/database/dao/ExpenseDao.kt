package com.mdshahsamir.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mdshahsamir.database.data.ExpenseDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Query("Select * from ExpenseDbModel")
    fun getAllExpenseCategories(): Flow<List<ExpenseDbModel>>

    @Insert
    fun addExpenseCategory(expenseDbModel: ExpenseDbModel)

    @Update
    fun updateExpenseCategory(expenseDbModel: ExpenseDbModel)

    @Delete
    fun deleteCategory(expenseDbModel: ExpenseDbModel)
}