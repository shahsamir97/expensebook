package com.mdshahsamir.expensebook.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mdshahsamir.expensebook.R
import com.mdshahsamir.expensebook.navigation.AppNavigation
import com.mdshahsamir.expensebook.navigation.NavigationScreen

@Composable
fun ExpenseBookApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = { BottomNavigation(navController = navController, currentRoute = currentRoute) }
    ) {
        Box(modifier = Modifier.padding(bottom = it.calculateBottomPadding())) {
            AppNavigation(navHostController = navController)
        }
    }
}

@Composable
fun BottomNavigation(navController: NavController, currentRoute: String?) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.surfaceBright,
        ) {
        NavigationBarItem(
            selected =  NavigationScreen.Dashboard.route == currentRoute,
            onClick = {
                navController.navigate(NavigationScreen.Dashboard.route) {
                    launchSingleTop = true
                }
            },
            icon = {
                Icon(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(R.drawable.ic_home),
                    contentDescription = stringResource(R.string.home)
                )
            },
            label = { Text(text = stringResource(R.string.home), style = MaterialTheme.typography.labelLarge) }
        )
        NavigationBarItem(
            selected = NavigationScreen.Transactions.route == currentRoute,
            onClick = {
                navController.navigate(NavigationScreen.Transactions.route) {
                    launchSingleTop = true
                }
            },
            icon = {
                Icon(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(R.drawable.ic_statement),
                    contentDescription = stringResource(R.string.transactions)
                )
            },
            label = { Text(text = stringResource(R.string.transactions), style = MaterialTheme.typography.labelLarge) }
        )
    }
}