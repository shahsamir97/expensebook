package com.mdshahsamir.expensebook.model

import java.io.Serializable

data class Expense(
    val id: Long = 0,
    val category: String = "",
    val budget: Float = 0f,
    val spendAmount: Float = 0f,
): Serializable {
    companion object {
        val Default = Expense(
            id = -1,
            category = "Miscellaneous",
            budget = 0f,
            spendAmount = 0f,
        )
    }
}
