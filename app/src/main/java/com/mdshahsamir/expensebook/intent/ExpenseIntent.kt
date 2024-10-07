package com.mdshahsamir.expensebook.intent

import com.mdshahsamir.expensebook.model.ExpenseState

sealed class ExpenseIntent {
    data class Spend(val amount: Float): ExpenseIntent()
    data class AddFund(val amount: Float): ExpenseIntent()
    data class AddCategory(val title: String, val budget: Float): ExpenseIntent()
    data class ShowInputDialog(val expenseState: ExpenseState): ExpenseIntent()
    data object HideInputDialog: ExpenseIntent()
    data object ShowAddCategoryDialog: ExpenseIntent()
    data object HideAddCategoryDialog: ExpenseIntent()
}
