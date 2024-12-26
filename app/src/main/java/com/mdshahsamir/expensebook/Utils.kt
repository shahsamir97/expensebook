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

fun String.toTimestamp(): Long {
    val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return try {
        val date = sdf.parse(this) // Parse the date string
        date?.time ?: 0 // Return the timestamp as Long, or null if parsing fails
    } catch (e: Exception) {
        e.printStackTrace()
        0
    }
}

fun String.isWithinLastDays(days: Int): Boolean {
    val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val inputDate = sdf.parse(this) ?: return false // Parse the input date or return false if invalid

    val calendar = Calendar.getInstance()
    calendar.time = Date() // Set to the current date
    calendar.add(Calendar.DAY_OF_YEAR, -days) // Subtract the specified number of days

    return inputDate.after(calendar.time) || inputDate == calendar.time
}

fun Float.toDisplayableNumberFormat(): String {
    return when {
        this >= 1_000_000 -> String.format("%.1fM", this / 1_000_000.0) // Millions (e.g., 1.2M)
        this >= 10_000 -> "${this / 1_000}K" // Thousands without decimals (e.g., 10K)
        else -> this.toString() // Less than 1K (e.g., 999)
    }
}

fun Float.toDisplayableNumberFormatForTransaction(): String {
    return when {
        this >= 1_000_000 -> String.format("%.1fM", this / 1_000_000.0) // Millions (e.g., 1.2M)
        else -> this.toString() // Less than 1K (e.g., 999)
    }
}

val TransactionFilterOptions = listOf(7,21,30)