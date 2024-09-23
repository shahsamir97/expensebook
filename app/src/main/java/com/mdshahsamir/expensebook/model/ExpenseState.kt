package com.mdshahsamir.expensebook.model

data class ExpenseState(
    val category: String = "Miscellaneous",
    val budget: Float = 0f,
    val spendAmount: Float = 0f,
)
