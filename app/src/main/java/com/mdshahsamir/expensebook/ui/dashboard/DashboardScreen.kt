package com.mdshahsamir.expensebook.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mdshahsamir.expensebook.R
import com.mdshahsamir.expensebook.convertToProgressBarValue
import com.mdshahsamir.expensebook.intent.ExpenseIntent
import com.mdshahsamir.expensebook.model.ExpenseState
import com.mdshahsamir.ui.CreateCategoryDialog
import com.mdshahsamir.ui.EditCategoryDialog
import com.mdshahsamir.ui.InputDialog
import com.mdshahsamir.ui.ProgressItem
import com.mdshahsamir.ui.theme.ExpenseBookTheme

@Composable
fun DashboardScreen(viewModel: DashboardViewModel = hiltViewModel()) {
    val expenseState by viewModel.listOfExpenseState.collectAsStateWithLifecycle()
    val showInputDialogState by viewModel.showInputDialogState.collectAsStateWithLifecycle()
    val showAddCategoryDialog by viewModel.showAddCategoryDialog.collectAsStateWithLifecycle()
    var showUpdateCategory by rememberSaveable { mutableStateOf(Pair(false, ExpenseState())) }


    DashboardContent(
        expenseState = expenseState,
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
        }
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
        showUpdateCategory.second.let { expenseState ->
            EditCategoryDialog(
                title = expenseState.category,
                budget = expenseState.budget,
                spend = expenseState.spendAmount,
                onClose = { showUpdateCategory = Pair(false, ExpenseState()) },
                onClickUpdateCategory = { title, budget, spend ->
                    val newValue = ExpenseState(
                        id = expenseState.id,
                        category = title,
                        budget = budget,
                        spendAmount = spend
                    )

                    viewModel.processIntent(ExpenseIntent.UpdateCategory(newValue))
                    showUpdateCategory = Pair(false, ExpenseState())
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardContent(
    expenseState: List<ExpenseState>,
    onClickItem: (expenseState: ExpenseState) -> Unit,
    onClickAddCategory: () -> Unit,
    onClickDelete: (expenseState: ExpenseState) -> Unit,
    onClickUpdateCategory: (expenseState: ExpenseState) -> Unit,
) {
    var showOptionsMenu by rememberSaveable { mutableStateOf(Pair(false, ExpenseState())) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Dashboard", color = MaterialTheme.colorScheme.onPrimary) },
                actions = {
                    if (showOptionsMenu.first) {
                        Row {
                            IconButton(onClick = {
                                onClickUpdateCategory(showOptionsMenu.second)
                                showOptionsMenu = Pair(false, ExpenseState())
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.Edit,
                                    contentDescription = "Edit",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                )
                            }
                            IconButton(onClick = {
                                onClickDelete(showOptionsMenu.second)
                                showOptionsMenu = Pair(false, ExpenseState())
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = "Delete",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    if (showOptionsMenu.first) {
                        IconButton(onClick = {
                            showOptionsMenu = Pair(false, ExpenseState())
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        },
        bottomBar = {
            BottomNavigation()
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(12.dp),
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
        }
    ) { contentPadding ->
        LazyVerticalGrid(
            modifier = Modifier
                .padding(contentPadding)
                .padding(16.dp),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.End,
            verticalArrangement = Arrangement.Center,
        ) {
            items(expenseState) { expense ->
                ProgressItem(
                    title = expense.category,
                    progress = convertToProgressBarValue(expense.spendAmount, expense.budget),
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

@Composable
fun BottomNavigation() {
    BottomAppBar {
        NavigationBarItem(
            selected = false,
            onClick = {},
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
            selected = true,
            onClick = {},
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

@Preview
@Composable
internal fun DashboardScreenPreview() {
    ExpenseBookTheme {
        DashboardContent(
            expenseState = listOf(
                ExpenseState(spendAmount = 500f, budget = 1000f),
                ExpenseState(spendAmount = 400f, budget = 4500f)
            ),
            onClickItem = {},
            onClickAddCategory = {},
            onClickDelete = {},
            onClickUpdateCategory = {}
        )
    }
}