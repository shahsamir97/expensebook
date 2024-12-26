package com.mdshahsamir.expensebook.model

import androidx.annotation.StringDef

data class TransactionData(
    val transactionId: Long = 0,
    val amount: Float,
    val category: String,
    val type: String,
    val time: String,
) {
    companion object {
        val DefaultData = TransactionData(
            transactionId = -1,
            time = "",
            category = "",
            amount = 0f,
            type = ""
        )
    }
}

@StringDef(TransactionMode.FUND_ADDED, TransactionMode.SPEND)
annotation class TransactionType

object TransactionMode {
    const val FUND_ADDED = "Add Fund"
    const val SPEND = "Spend"
}