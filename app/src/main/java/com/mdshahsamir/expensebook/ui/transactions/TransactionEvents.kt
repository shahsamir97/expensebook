package com.mdshahsamir.expensebook.ui.transactions

import com.mdshahsamir.expensebook.model.TransactionData

interface TransactionEvents {
    fun selectTransaction(transactionData: TransactionData)
    fun deleteTransaction()
    fun onPressBack()
    fun filterTransaction(filter: Int)
    fun clearAllTransaction()
}