package com.mdshahsamir.expensebook.ui.transactions

import com.mdshahsamir.expensebook.model.TransactionData

data class TransactionsState(
    val list: List<TransactionData>,
    val selectedTransaction: TransactionData,
    val showDeleteOption: Boolean,
    val selectedFilter: Int,
) {
    companion object {
        val DefaultState = TransactionsState(
            list = emptyList(),
            selectedTransaction = TransactionData.DefaultData,
            showDeleteOption = false,
            selectedFilter = Int.MAX_VALUE
        )
    }
}