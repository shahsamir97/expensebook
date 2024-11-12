package com.mdshahsamir.expensebook

import com.mdshahsamir.database.data.ExpenseDbModel
import com.mdshahsamir.expensebook.model.Expense
import java.text.SimpleDateFormat
import java.util.Calendar
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

fun String.isWithinLastDays(days: Int): Boolean {
    val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val inputDate = sdf.parse(this) ?: return false // Parse the input date or return false if invalid

    val calendar = Calendar.getInstance()
    calendar.time = Date() // Set to the current date
    calendar.add(Calendar.DAY_OF_YEAR, -days) // Subtract the specified number of days

    return inputDate.after(calendar.time) || inputDate == calendar.time
}

val TransactionFilterOptions = listOf(7,21,30)