package com.mdshahsamir.expensebook.ui.transactions

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mdshahsamir.expensebook.R
import com.mdshahsamir.expensebook.model.TransactionData
import com.mdshahsamir.expensebook.model.TransactionMode
import com.mdshahsamir.ui.EbTextView
import com.mdshahsamir.ui.theme.AddFundColor
import com.mdshahsamir.ui.theme.ExpenseBookTheme
import com.mdshahsamir.ui.theme.SpendColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(transactionsState: TransactionsState, events: TransactionEvents) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.transactions),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                actions = {
                    if (transactionsState.showDeleteOption) {
                        Row {
                            IconButton(onClick = { events.deleteTransaction() }) {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = stringResource(R.string.edit),
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    if (transactionsState.showDeleteOption) {
                        IconButton(onClick = { events.onPressBack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back),
                                tint = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                    }
                },
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) {
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(top = it.calculateTopPadding()),
            contentPadding = PaddingValues(16.dp)
            ) {
            items(transactionsState.list) { transactionData ->
                TransactionListItem(transactionData, transactionsState, events)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionListItem(
    transactionData: TransactionData,
    state: TransactionsState,
    events: TransactionEvents,
) {
    Card(
        modifier = Modifier.combinedClickable(
            onLongClick = { events.selectTransaction(transactionData) },
            onClick = {}
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (state.selectedTransaction?.transactionId == transactionData.transactionId) {
                MaterialTheme.colorScheme.surfaceVariant
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transactionData.type.uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    color = if (transactionData.type.equals(TransactionMode.SPEND)) SpendColor else AddFundColor
                )
                EbTextView(
                    text = stringResource(R.string.category_colon_x,transactionData.category).uppercase(),
                    style = MaterialTheme.typography.titleSmall,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Text(
                modifier = Modifier.weight(1f),
                text = transactionData.time,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.weight(1f),
                text = transactionData.amount.toString(),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.End
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
                        transactionId = 1L,
                        type = TransactionMode.FUND_ADDED,
                        amount = 150.0F,
                        category = "Books sda hjskdak jsdhkahsdk jishdkashdjk",
                        time = "23.09.2024"
                    )
                ),
                selectedTransaction = TransactionData.DefaultData.copy(transactionId = 0),
                showDeleteOption = true
            ),
            events = object : TransactionEvents {
                override fun selectTransaction(transactionData: TransactionData) {}
                override fun deleteTransaction() {}
                override fun onPressBack() {}
            }
        )
    }
}