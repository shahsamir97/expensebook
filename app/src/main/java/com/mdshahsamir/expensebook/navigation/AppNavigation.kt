package com.mdshahsamir.expensebook.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mdshahsamir.expensebook.ui.dashboard.DashboardScreen
import com.mdshahsamir.expensebook.ui.dashboard.DashboardViewModel
import com.mdshahsamir.expensebook.ui.transactions.TransactionsScreen

@Composable
fun AppNavigation(navHostController: NavHostController) {
    val viewModel: DashboardViewModel = hiltViewModel()

    NavHost(navController = navHostController, startDestination = NavigationScreen.Dashboard.route) {

        composable(NavigationScreen.Dashboard.route) {
            DashboardScreen(viewModel)
        }

        composable(NavigationScreen.Transactions.route) {
            val state by viewModel.transactionState.collectAsStateWithLifecycle()

            TransactionsScreen(state, viewModel)
        }
    }
}