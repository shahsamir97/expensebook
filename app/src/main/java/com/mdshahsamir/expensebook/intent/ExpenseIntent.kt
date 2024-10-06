package com.mdshahsamir.expensebook.intent

sealed class ExpenseIntent {
    data class Spend(val amount: Float): ExpenseIntent()
    data class AddFund(val amount: Float): ExpenseIntent()
    data class AddCategory(val title: String, val budget: Float): ExpenseIntent()
    data class ShowInputDialog(val expenseIndex: Int): ExpenseIntent()
    data object HideInputDialog: ExpenseIntent()
    data object ShowAddCategoryDialog: ExpenseIntent()
    data object HideAddCategoryDialog: ExpenseIntent()
}
