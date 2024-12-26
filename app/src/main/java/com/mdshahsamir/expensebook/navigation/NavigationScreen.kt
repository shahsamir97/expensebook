package com.mdshahsamir.expensebook.navigation

import androidx.annotation.StringDef

@StringDef(Route.DASHBOARD, Route.TRANSACTIONS)
annotation class RouteValue

object Route {
    const val DASHBOARD = "dashboard"
    const val TRANSACTIONS = "transactions"
}

sealed class NavigationScreen(@RouteValue val route: String) {
    data object Dashboard : NavigationScreen(Route.DASHBOARD)
    data object Transactions : NavigationScreen(Route.TRANSACTIONS)
}

