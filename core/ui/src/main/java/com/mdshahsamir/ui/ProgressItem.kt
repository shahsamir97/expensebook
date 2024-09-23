package com.mdshahsamir.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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

@Composable
fun ProgressItem(title: String, progress: Float, amount: Float, budget: Float, onClick: () -> Unit) {

    ElevatedCard(
        modifier = Modifier.padding(8.dp).fillMaxSize(),
        colors = CardDefaults.elevatedCardColors(),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                progress = { progress },
                strokeWidth = 12.dp,
                trackColor = MaterialTheme.colorScheme.inversePrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "$amount/$budget", style = MaterialTheme.typography.labelSmall)
            Text(text = title, style = MaterialTheme.typography.titleMedium)
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
        onClick = {}
    )
}