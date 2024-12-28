package com.mdshahsamir.expensebook

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
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

fun getStartDateOfLastDays(days: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    calendar.add(Calendar.DAY_OF_YEAR, -days)

    return calendar.timeInMillis
}

fun isTimeWithinRange(startDate: Long, endDate: Long, inputTime: Long): Boolean {
    return inputTime in startDate..endDate
}

fun Uri.toBitmap(context: Context): Bitmap? {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, this)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, this)
        }
    } catch (e: Exception) {
        null
    }
}