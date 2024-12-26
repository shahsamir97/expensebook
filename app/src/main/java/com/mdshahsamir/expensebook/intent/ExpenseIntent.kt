package com.mdshahsamir.expensebook.intent

import com.mdshahsamir.expensebook.model.Expense

sealed class ExpenseIntent {
    data class Spend(val amount: Float): ExpenseIntent()
    data class AddFund(val amount: Float): ExpenseIntent()
    data class AddCategory(val title: String, val budget: Float): ExpenseIntent()
    data class DeleteCategory(val expense: Expense): ExpenseIntent()
    data class UpdateCategory(val expense: Expense): ExpenseIntent()
    data class ShowInputDialog(val expense: Expense): ExpenseIntent()
    data object HideInputDialog: ExpenseIntent()
    data object ShowAddCategoryDialog: ExpenseIntent()
    data object HideAddCategoryDialog: ExpenseIntent()
}
