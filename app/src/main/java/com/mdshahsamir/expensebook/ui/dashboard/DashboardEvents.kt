package com.mdshahsamir.expensebook.ui.dashboard

interface DashboardEvents {
    fun saveIncomeInput(amount: Float)
    fun onClickResetCategory()
    fun onConfirmResetCategory()
}