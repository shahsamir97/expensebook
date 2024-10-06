package com.mdshahsamir.expensebook

fun convertToProgressBarValue(spendAmount: Float, budget: Float): Float {
    val scalingFactor = 100/budget
    return (scalingFactor * spendAmount)/100
}