package com.mdshahsamir.expensebook.ui.transactions

import com.mdshahsamir.expensebook.model.TransactionData

data class TransactionsState(
    val list: List<TransactionData>,
) {
    companion object {
        val DefaultState = TransactionsState(emptyList())
    }
}