package com.mdshahsamir.expensebook.ui.dashboard

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DashboardScreen() {
    Scaffold { contentPadding ->
        LazyVerticalGrid(
            modifier = Modifier.padding(contentPadding),
            columns = GridCells.Adaptive(minSize = 128.dp),
        ) {

        }
    }
}