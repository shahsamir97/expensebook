package com.mdshahsamir.expensebook.model

enum class TransactionFilterPreset(val days: Int) {
    LAST_7DAYS(7),
    LAST_21DAYS(21),
    LAST_30DAYS(30)
}