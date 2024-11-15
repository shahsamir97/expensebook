package com.mdshahsamir.expensebook.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdshahsamir.expensebook.intent.ExpenseIntent
import com.mdshahsamir.expensebook.isWithinLastDays
import com.mdshahsamir.expensebook.model.Expense
import com.mdshahsamir.expensebook.model.TransactionData
import com.mdshahsamir.expensebook.model.TransactionMode
import com.mdshahsamir.expensebook.ui.transactions.TransactionEvents
import com.mdshahsamir.expensebook.ui.transactions.TransactionsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository
) : ViewModel(), TransactionEvents, DashboardEvents {

    private val _dashboardState = MutableStateFlow(DashboardState.DefaultState)
    val dashboardState: StateFlow<DashboardState> = _dashboardState

    private val _listOfExpense = MutableStateFlow(listOf<Expense>())
    val listOfExpense: StateFlow<List<Expense>> = _listOfExpense

    private val _showInputDialogState = MutableStateFlow(Pair(false, Expense()))
    val showInputDialogState: StateFlow<Pair<Boolean, Expense>> = _showInputDialogState

    private val _showAddCategoryDialog = MutableStateFlow(false)
    val showAddCategoryDialog: StateFlow<Boolean> = _showAddCategoryDialog

    private val _showOptionsMenu = MutableStateFlow(false)
    val showOptionsMenu: StateFlow<Boolean> = _showOptionsMenu

    private val _transactionState = MutableStateFlow(TransactionsState.DefaultState)
    val transactionState: StateFlow<TransactionsState> = _transactionState

    private var selectedExpense = Expense()

    private val selectedTransactions = ArrayList<TransactionData>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            dashboardRepository.getAllCategories().collectLatest { listOfExpenseState ->
                _listOfExpense.value = listOfExpenseState
                _dashboardState.update { it.copy(totalSpend = listOfExpense.value.sumOf { it.spendAmount.toDouble() }.toFloat()) }
            }
        }

        viewModelScope.launch {
            _dashboardState.update { it.copy(income = dashboardRepository.getIncomeAmount()) }
        }

        retrieveTransactions()
    }

    private fun retrieveTransactions() {
        viewModelScope.launch(Dispatchers.IO) {
            dashboardRepository.getAllTransaction().collectLatest { listOfTransactions ->
                _transactionState.update {
                    it.copy(list = listOfTransactions.filter {
                        it.time.isWithinLastDays(
                            transactionState.value.selectedFilter
                        )
                    })
                }
            }
        }
    }

    fun processIntent(intent: ExpenseIntent) {
        when(intent) {
            is ExpenseIntent.Spend -> spend(intent)
            is ExpenseIntent.AddFund -> addFund(intent)
            is ExpenseIntent.AddCategory -> addCategory(intent)

            is ExpenseIntent.ShowInputDialog -> {
                selectedExpense = intent.expense
                _showInputDialogState.value = Pair(true, selectedExpense)
            }

            ExpenseIntent.HideInputDialog -> _showInputDialogState.value = Pair(false, Expense())

            ExpenseIntent.ShowAddCategoryDialog -> _showAddCategoryDialog.value = true

            ExpenseIntent.HideAddCategoryDialog -> _showAddCategoryDialog.value = false

            is ExpenseIntent.DeleteCategory -> deleteCategory(intent)
            is ExpenseIntent.UpdateCategory -> updateCategory(intent)
        }
    }

    private fun addCategory(intent: ExpenseIntent.AddCategory) {
        val expense = Expense(
            category = intent.title,
            budget = intent.budget,
            spendAmount = 0f
        )

        viewModelScope.launch {
            dashboardRepository.addCategory(expense)
        }
    }

    private fun deleteCategory(intent: ExpenseIntent.DeleteCategory) {
        viewModelScope.launch {
            dashboardRepository.deleteCategory(intent.expense)
        }
    }

    private fun updateCategory(intent: ExpenseIntent.UpdateCategory) {
        viewModelScope.launch {
            dashboardRepository.updateCategory(intent.expense)
        }
    }

    private fun addFund(intent: ExpenseIntent.AddFund) {
        selectedExpense.let { expense ->
            val newBudget = expense.budget + intent.amount
            val newValue = expense.copy(
                budget = newBudget,
                spendAmount = expense.spendAmount.let { if (it < 0) it.plus(intent.amount) else it }
            )

            viewModelScope.launch {
                dashboardRepository.updateCategory(newValue)
                dashboardRepository.addTransaction(expense, TransactionMode.FUND_ADDED, intent.amount)
            }
        }
    }

    private fun spend(intent: ExpenseIntent.Spend) {
        val newValue =
            selectedExpense.copy(spendAmount = selectedExpense.spendAmount + intent.amount)

        viewModelScope.launch {
            dashboardRepository.updateCategory(newValue)
            dashboardRepository.addTransaction(newValue, TransactionMode.SPEND, intent.amount)
        }
    }

    override fun selectTransaction(transactionData: TransactionData) {
        viewModelScope.launch {
            selectedTransactions.add(transactionData)
            _transactionState.update {
                it.copy(
                    selectedTransactions = selectedTransactions,
                    showDeleteOption = true,
                )
            }
        }
    }

    override fun deleteTransaction() {
        viewModelScope.launch {
            dashboardRepository.deleteTransaction(selectedTransactions)
            _transactionState.update { it.copy(
                showDeleteOption = false,
                selectedTransactions = emptyList(),
            ) }

            selectedTransactions.clear()
        }
    }

    override fun onPressBack() {
        _transactionState.update {
            it.copy(
                showDeleteOption = false,
                selectedTransactions = emptyList(),
            )
        }
        selectedTransactions.clear()
    }

    override fun filterTransaction(filter: Int) {
        if (filter == transactionState.value.selectedFilter) {
            retrieveTransactions()
            _transactionState.update {
                it.copy(
                    selectedFilter = TransactionsState.DefaultState.selectedFilter
                )
            }
        } else {
            retrieveTransactions()
            _transactionState.update {
                it.copy(
                    selectedFilter = filter
                )
            }
        }
    }

    override fun clearAllTransaction() {
        viewModelScope.launch {
            dashboardRepository.deleteTransaction(transactionState.value.list)
        }
    }

    override fun saveIncomeInput(amount: Float) {
        viewModelScope.launch {
            dashboardRepository.setIncome(amount)

            val income  = dashboardRepository.getIncomeAmount()
            _dashboardState.update { it.copy(income = income) }
        }
    }
}
