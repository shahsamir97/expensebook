package com.mdshahsamir.expensebook

import com.mdshahsamir.database.data.ExpenseDbModel
import com.mdshahsamir.expensebook.model.Expense
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun convertToProgressBarValue(spendAmount: Float, budget: Float): Float {
    val scalingFactor = 100/budget
    return (scalingFactor * spendAmount)/100
}

fun Expense.toExpense(): ExpenseDbModel = ExpenseDbModel(
    uid = this.id,
    category = this.category,
    budget = this.budget,
    spend = this.spendAmount
)

fun Long.toUiDateFormat(): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val date = Date(this)
    return sdf.format(date)
}