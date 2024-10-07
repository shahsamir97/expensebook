package com.mdshahsamir.expensebook

import com.mdshahsamir.database.data.Expense
import com.mdshahsamir.expensebook.model.ExpenseState

fun convertToProgressBarValue(spendAmount: Float, budget: Float): Float {
    val scalingFactor = 100/budget
    return (scalingFactor * spendAmount)/100
}

fun ExpenseState.toExpense(): Expense = Expense(
    uid = this.id,
    category = this.category,
    budget = this.budget,
    spend = this.spendAmount
)