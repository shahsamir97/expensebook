package com.mdshahsamir.expensebook.model

data class TransactionFilter(
    val startDate: Long = Long.MIN_VALUE,
    val endDate: Long = Long.MAX_VALUE,
)
