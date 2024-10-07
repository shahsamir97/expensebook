package com.mdshahsamir.expensebook.model

data class ExpenseState(
    val id: Long = 0,
    val category: String = "Miscellaneous",
    val budget: Float = 0f,
    val spendAmount: Float = 0f,
)
