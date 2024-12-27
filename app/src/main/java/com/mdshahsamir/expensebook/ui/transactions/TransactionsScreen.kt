package com.mdshahsamir.expensebook.ui.transactions

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mdshahsamir.expensebook.R
import com.mdshahsamir.expensebook.getStartDateOfLastDays
import com.mdshahsamir.expensebook.model.TransactionData
import com.mdshahsamir.expensebook.model.TransactionFilter
import com.mdshahsamir.expensebook.model.TransactionFilterPreset
import com.mdshahsamir.expensebook.model.TransactionMode
import com.mdshahsamir.expensebook.toDisplayableNumberFormatForTransaction
import com.mdshahsamir.expensebook.toUiDateFormat
import com.mdshahsamir.ui.EbAlertDialog
import com.mdshahsamir.ui.EbDateRangePicker
import com.mdshahsamir.ui.EbTextView
import com.mdshahsamir.ui.theme.AddFundColor
import com.mdshahsamir.ui.theme.ExpenseBookTheme
import com.mdshahsamir.ui.theme.SpendColor


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TransactionsScreen(transactionsState: TransactionsState, events: TransactionEvents) {

    var showOptionsMenu by rememberSaveable { mutableStateOf(false) }
    var showClearTransactionAlert by rememberSaveable { mutableStateOf(false) }
    var showDatePickerDialog by rememberSaveable { mutableStateOf(false) }

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
                    Row {
                        if (transactionsState.showDeleteOption) {
                            IconButton(onClick = { events.deleteTransaction() }) {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = stringResource(R.string.delete),
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                )
                            }
                        }

                        Box {
                            IconButton(onClick = { showOptionsMenu = true }) {
                                Icon(
                                    imageVector = Icons.Outlined.MoreVert,
                                    contentDescription = stringResource(R.string.edit),
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                )
                            }
                            DropdownMenu(
                                expanded = showOptionsMenu,
                                onDismissRequest = { showOptionsMenu = false }) {
                                DropdownMenuItem(
                                    text = { Text(text = stringResource(R.string.clear_all_transactions)) },
                                    onClick = {
                                        showOptionsMenu = false
                                        showClearTransactionAlert = true
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Outlined.Delete,
                                            contentDescription = stringResource(R.string.clear_all_transactions),
                                        )
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text(text = stringResource(R.string.filter_transactions)) },
                                    onClick = {
                                        showOptionsMenu = false
                                        showDatePickerDialog = true
                                    },
                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_filter_list),
                                            contentDescription = stringResource(R.string.delete),
                                        )
                                    }
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
    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding()),
            verticalArrangement = Arrangement.Center
            ) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                if (transactionsState.showCustomFilter) {
                    FilterChip(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        colors = FilterChipDefaults.filterChipColors(containerColor = MaterialTheme.colorScheme.surface),
                        selected = true,
                        onClick = { events.onDateRangeSelected(transactionsState.selectedFilter)},
                        label = { Text(text = "From: ${transactionsState.selectedFilter.startDate.toUiDateFormat()} - To: ${transactionsState.selectedFilter.startDate.toUiDateFormat()}") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = "From: ${transactionsState.selectedFilter.startDate.toUiDateFormat()} - To: ${transactionsState.selectedFilter.startDate.toUiDateFormat()}",
                            )
                        }
                    )
                }

                TransactionFilterPreset.entries.forEach {
                    FilterChip(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        colors = FilterChipDefaults.filterChipColors(containerColor = MaterialTheme.colorScheme.surface),
                        selected = transactionsState.selectedFilter.startDate == getStartDateOfLastDays(it.days),
                        onClick = { events.filterTransaction(it.days) },
                        label = { Text(text = stringResource(id = R.string.last_x_days, it.days)) },
                        leadingIcon = {
                            if (transactionsState.selectedFilter.startDate == getStartDateOfLastDays(it.days)) {
                                Icon(
                                    imageVector = Icons.Outlined.Check,
                                    contentDescription = stringResource(
                                        id = R.string.filter_by_last_x_days,
                                        it.days
                                    ),
                                )
                            }
                        }
                    )
                }
            }

            if (transactionsState.list.isEmpty()) {
                Box( modifier = Modifier.weight(1f)) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center),
                        text = stringResource(R.string.no_transactions_yet),
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.3f),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center,
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(transactionsState.list) { transactionData ->
                        TransactionListItem(transactionData, transactionsState, events)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }

    if (showClearTransactionAlert) {
        EbAlertDialog(
            title = stringResource(R.string.are_you_sure),
            bodyText = stringResource(R.string.once_you_delete_all_transaction),
            onClickConfirm = {
                events.clearAllTransaction()
                showClearTransactionAlert = false
            },
            onClickDismiss = { showClearTransactionAlert = false }
        )
    }

    if (showDatePickerDialog) {
        EbDateRangePicker(
            onDateRangeSelected = { dateRange ->
                showDatePickerDialog = false

                if (dateRange.first != null && dateRange.second != null) {
                    events.onDateRangeSelected(TransactionFilter(dateRange.first!!, dateRange.second!!))
                }
            },
            onDismiss = { showDatePickerDialog = false }
        )
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
            containerColor = if (state.isTransactionSelected(transactionData)) {
                MaterialTheme.colorScheme.surfaceVariant
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
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
                text = transactionData.amount.toDisplayableNumberFormatForTransaction(),
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
                selectedTransactions = listOf(TransactionData.DefaultData.copy(transactionId = 0)),
                showDeleteOption = true,
                selectedFilter = TransactionFilter(endDate = Long.MAX_VALUE),
                showDatePicker = false,
                showCustomFilter = false,
            ),
            events = object : TransactionEvents {
                override fun selectTransaction(transactionData: TransactionData) {}
                override fun deleteTransaction() {}
                override fun onPressBack() {}
                override fun filterTransaction(filter: Int) {}
                override fun clearAllTransaction() {}
                override fun onDateRangeSelected(transactionFilter: TransactionFilter) {}
            }
        )
    }
}