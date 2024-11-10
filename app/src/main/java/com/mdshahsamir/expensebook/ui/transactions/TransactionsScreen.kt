package com.mdshahsamir.expensebook.ui.transactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mdshahsamir.expensebook.R
import com.mdshahsamir.expensebook.model.TransactionData
import com.mdshahsamir.expensebook.model.TransactionMode
import com.mdshahsamir.ui.theme.AddFundColor
import com.mdshahsamir.ui.theme.ExpenseBookTheme
import com.mdshahsamir.ui.theme.SpendColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(transactionsState: TransactionsState) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.transactions),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) {
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(it)
            .padding(16.dp)) {
            items(transactionsState.list) { transactionData ->
                TransactionListItem(transactionData)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun TransactionListItem(transactionData: TransactionData) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = transactionData.type.uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    color = if (transactionData.type.equals(TransactionMode.SPEND)) SpendColor else AddFundColor
                )
                Text(
                    text = stringResource(R.string.category_colon_x,transactionData.category).uppercase(),
                    style = MaterialTheme.typography.titleSmall,
                )
            }
            Text(text = transactionData.time, style = MaterialTheme.typography.titleSmall)
            Text(
                text = transactionData.amount.toString(),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Preview
@Composable
fun TransactionsScreenPreview() {
    ExpenseBookTheme {
        TransactionsScreen(
            TransactionsState(
                list = listOf(
                    TransactionData(
                        transactionId = 0L,
                        type = TransactionMode.SPEND,
                        amount = 200.0F,
                        category = "Books",
                        time = "23.09.2024"
                    ),
                    TransactionData(
                        transactionId = 0L,
                        type = TransactionMode.FUND_ADDED,
                        amount = 150.0F,
                        category = "Books",
                        time = "23.09.2024"
                    )
                )
            )
        )
    }
}