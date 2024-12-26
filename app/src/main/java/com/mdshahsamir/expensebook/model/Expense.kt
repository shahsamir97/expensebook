package com.mdshahsamir.expensebook.model

data class Expense(
    val id: Long = 0,
    val category: String = "",
    val budget: Float = 0f,
    val spendAmount: Float = 0f,
) {
    companion object {
        val Default = Expense(
            id = -1,
            category = "Miscellaneous",
            budget = 0f,
            spendAmount = 0f,
        )
    }
}
