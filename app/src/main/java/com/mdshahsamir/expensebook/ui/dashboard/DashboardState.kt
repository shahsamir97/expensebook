package com.mdshahsamir.expensebook.ui.dashboard

import com.mdshahsamir.expensebook.model.Expense

data class DashboardState(
    val listOfExpense: List<Expense>,
    val showOptionsMenu: Boolean,
    val selectedExpenseItem: Expense,
    val showInputDialog: Boolean,
    val showCreateCategoryDialog: Boolean,
    val showEditCategoryDialog: Boolean,
    val income: Float,
    val totalSpend: Float,
) {
    companion object {
        val DefaultState = DashboardState(
            listOfExpense = emptyList(),
            showInputDialog = false,
            selectedExpenseItem = Expense.Default,
            showOptionsMenu = false,
            showEditCategoryDialog = false,
            showCreateCategoryDialog = false,
            income = 0f,
            totalSpend = 0f,
        )
    }
}