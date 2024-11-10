package com.mdshahsamir.expensebook.ui.dashboard

import com.mdshahsamir.expensebook.model.Expense

data class DashboardState(
    val listOfExpense: List<Expense>,
    val showOptionsMenu: Boolean,
    val selectedExpenseItem: Expense,
    val showInputDialog: Boolean,
    val showCreateCategoryDialog: Boolean,
    val showEditCategoryDialog: Boolean,
)