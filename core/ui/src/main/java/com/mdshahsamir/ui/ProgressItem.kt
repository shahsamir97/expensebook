package com.mdshahsamir.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProgressItem(
    title: String,
    progress: Float,
    amount: Float,
    budget: Float,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    isSelected: Boolean,
) {
    ElevatedCard (
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceContainer
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(40.dp),
                progress = { progress },
                strokeWidth = 8.dp,
                trackColor = MaterialTheme.colorScheme.inversePrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "$amount/$budget", style = MaterialTheme.typography.labelLarge)
            Text(text = title, style = MaterialTheme.typography.titleLarge)
        }
    }
}

@Preview
@Composable
fun ProgressItemPreview() {
    ProgressItem(
        title = "Grocery",
        progress = .7f,
        amount = 100f,
        budget = 200f,
        onClick = {},
        onLongClick = {},
        isSelected = false
    )
}