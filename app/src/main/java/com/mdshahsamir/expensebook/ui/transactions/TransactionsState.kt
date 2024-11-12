package com.mdshahsamir.expensebook.ui.transactions

import com.mdshahsamir.expensebook.model.TransactionData

data class TransactionsState(
    val list: List<TransactionData>,
    val selectedTransactions: List<TransactionData>,
    val showDeleteOption: Boolean,
    val selectedFilter: Int,
) {
    companion object {
        val DefaultState = TransactionsState(
            list = emptyList(),
            selectedTransactions = emptyList(),
            showDeleteOption = false,
            selectedFilter = Int.MAX_VALUE
        )
    }

    fun isTransactionSelected(transactionData: TransactionData): Boolean {
        return selectedTransactions.find { it.transactionId == transactionData.transactionId } != null
    }
}