package com.mdshahsamir.expensebook.ui.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mdshahsamir.expensebook.R
import com.mdshahsamir.expensebook.convertToProgressBarValue
import com.mdshahsamir.expensebook.intent.ExpenseIntent
import com.mdshahsamir.expensebook.model.Expense
import com.mdshahsamir.ui.CreateCategoryDialog
import com.mdshahsamir.ui.EditCategoryDialog
import com.mdshahsamir.ui.InputDialog
import com.mdshahsamir.ui.ProgressItem
import com.mdshahsamir.ui.theme.ExpenseBookTheme

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel
) {
    val expenseState by viewModel.listOfExpense.collectAsStateWithLifecycle()
    val showInputDialogState by viewModel.showInputDialogState.collectAsStateWithLifecycle()
    val showAddCategoryDialog by viewModel.showAddCategoryDialog.collectAsStateWithLifecycle()
    var showUpdateCategory by rememberSaveable { mutableStateOf(Pair(false, Expense())) }

    DashboardContent(
        expenseList = expenseState,
        onClickItem = { expense ->
            viewModel.processIntent(ExpenseIntent.ShowInputDialog(expense))
        },
        onClickAddCategory = {
            viewModel.processIntent(ExpenseIntent.ShowAddCategoryDialog)
        },
        onClickDelete = { expense ->
            viewModel.processIntent(ExpenseIntent.DeleteCategory(expense))
        },
        onClickUpdateCategory = { expense ->
            showUpdateCategory = Pair(true, expense)
        },
    )

    if (showInputDialogState.first) {
        InputDialog(
            title = showInputDialogState.second.category,
            onClickSpend = { spendAmount ->
                viewModel.processIntent(ExpenseIntent.Spend(spendAmount))
                viewModel.processIntent(ExpenseIntent.HideInputDialog)
            },
            onClickAddFund = { fundAmount ->
                viewModel.processIntent(ExpenseIntent.AddFund(fundAmount))
                viewModel.processIntent(ExpenseIntent.HideInputDialog)
            },
            onClose = {
                viewModel.processIntent(ExpenseIntent.HideInputDialog)
            }
        )
    }

    if(showAddCategoryDialog) {
        CreateCategoryDialog(
            onClickAddCategory = { title, budget ->
                viewModel.processIntent(ExpenseIntent.AddCategory(title, budget))
                viewModel.processIntent(ExpenseIntent.HideAddCategoryDialog)
            },
            onClose = { viewModel.processIntent(ExpenseIntent.HideAddCategoryDialog) }
        )
    }

    if (showUpdateCategory.first) {
        showUpdateCategory.second.let { expense ->
            EditCategoryDialog(
                title = expense.category,
                budget = expense.budget,
                spend = expense.spendAmount,
                onClose = { showUpdateCategory = Pair(false, Expense()) },
                onClickUpdateCategory = { title, budget, spend ->
                    val newValue = Expense(
                        id = expense.id,
                        category = title,
                        budget = budget,
                        spendAmount = spend
                    )

                    viewModel.processIntent(ExpenseIntent.UpdateCategory(newValue))
                    showUpdateCategory = Pair(false, Expense())
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardContent(
    expenseList: List<Expense>,
    onClickItem: (expense: Expense) -> Unit,
    onClickAddCategory: () -> Unit,
    onClickDelete: (expense: Expense) -> Unit,
    onClickUpdateCategory: (expense: Expense) -> Unit,
) {
    var showOptionsMenu by rememberSaveable { mutableStateOf(Pair(false, Expense())) }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.defaultMinSize(),
                title = {
                    Text(
                        text = stringResource(R.string.dashboard),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                actions = {
                    if (showOptionsMenu.first) {
                        Row {
                            IconButton(onClick = {
                                onClickUpdateCategory(showOptionsMenu.second)
                                showOptionsMenu = Pair(false, Expense())
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.Edit,
                                    contentDescription = stringResource(R.string.edit),
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                )
                            }
                            IconButton(onClick = {
                                onClickDelete(showOptionsMenu.second)
                                showOptionsMenu = Pair(false, Expense())
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = stringResource(R.string.delete),
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    if (showOptionsMenu.first) {
                        IconButton(onClick = {
                            showOptionsMenu = Pair(false, Expense())
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back),
                                tint = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onClickAddCategory
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(R.string.add_category)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = stringResource(id = R.string.add_category))
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) { contentPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(top = contentPadding.calculateTopPadding())) {
            if (expenseList.isEmpty()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.add_a_category_with_budget),
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.3f),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center,
                )
            } else {
                LazyVerticalStaggeredGrid(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    columns = StaggeredGridCells.Fixed(2),
                ) {
                    items(expenseList) { expense ->
                        ProgressItem(
                            title = expense.category,
                            progress = convertToProgressBarValue(
                                expense.spendAmount,
                                expense.budget
                            ),
                            amount = expense.spendAmount,
                            budget = expense.budget,
                            onClick = { onClickItem(expense) },
                            onLongClick = {
                                showOptionsMenu = Pair(true, expense)
                            },
                            isSelected = showOptionsMenu.first && showOptionsMenu.second.id == expense.id
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
internal fun DashboardScreenPreview() {
    ExpenseBookTheme {
        DashboardContent(
            expenseList = listOf(

            ),
            onClickItem = {},
            onClickAddCategory = {},
            onClickDelete = {},
            onClickUpdateCategory = {}
        )
    }
}