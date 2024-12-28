package com.mdshahsamir.expensebook.ui.transactions

import com.mdshahsamir.expensebook.model.TransactionData
import com.mdshahsamir.expensebook.model.TransactionFilter

interface TransactionEvents {
    fun selectTransaction(transactionData: TransactionData)
    fun deleteTransaction()
    fun onPressBack()
    fun filterTransaction(filter: Int)
    fun clearAllTransaction()
    fun onDateRangeSelected(transactionFilter: TransactionFilter)
    fun onClickExportPDF()
    fun resetToastMessage()
}