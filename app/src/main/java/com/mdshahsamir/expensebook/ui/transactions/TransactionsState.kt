package com.mdshahsamir.expensebook.ui.transactions

import com.mdshahsamir.expensebook.model.TransactionData
import com.mdshahsamir.expensebook.model.TransactionFilter

data class TransactionsState(
    val list: List<TransactionData>,
    val selectedTransactions: List<TransactionData>,
    val showDeleteOption: Boolean,
    val selectedFilter: TransactionFilter,
    val showDatePicker: Boolean,
    val showCustomFilter: Boolean,
    val showToastMessage: String = ""
) {
    companion object {
        val DefaultState = TransactionsState(
            list = emptyList(),
            selectedTransactions = emptyList(),
            showDeleteOption = false,
            selectedFilter = TransactionFilter(),
            showDatePicker = false,
            showCustomFilter = false,
        )
    }

    fun isTransactionSelected(transactionData: TransactionData): Boolean {
        return selectedTransactions.find { it.transactionId == transactionData.transactionId } != null
    }
}