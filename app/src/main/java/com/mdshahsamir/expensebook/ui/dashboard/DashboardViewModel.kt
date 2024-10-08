package com.mdshahsamir.expensebook.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdshahsamir.expensebook.intent.ExpenseIntent
import com.mdshahsamir.expensebook.model.ExpenseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository
) : ViewModel() {

    private val _listOfExpenseState = MutableStateFlow(listOf<ExpenseState>())
    val listOfExpenseState: StateFlow<List<ExpenseState>> = _listOfExpenseState

    private val _showInputDialogState = MutableStateFlow(Pair(false, ExpenseState()))
    val showInputDialogState: StateFlow<Pair<Boolean, ExpenseState>> = _showInputDialogState

    private val _showAddCategoryDialog = MutableStateFlow(false)
    val showAddCategoryDialog: StateFlow<Boolean> = _showAddCategoryDialog

    private val _showOptionsMenu = MutableStateFlow(false)
    val showOptionsMenu: StateFlow<Boolean> = _showOptionsMenu

    private var selectedExpense = ExpenseState()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            dashboardRepository.getAllCategories().collectLatest { listOfExpenseState ->
                _listOfExpenseState.value = listOfExpenseState
            }
        }
    }

    fun processIntent(intent: ExpenseIntent) {
        when(intent) {
            is ExpenseIntent.Spend -> spend(intent)
            is ExpenseIntent.AddFund -> addFund(intent)
            is ExpenseIntent.AddCategory -> addCategory(intent)

            is ExpenseIntent.ShowInputDialog -> {
                selectedExpense = intent.expenseState
                _showInputDialogState.value = Pair(true, selectedExpense)
            }

            ExpenseIntent.HideInputDialog -> _showInputDialogState.value = Pair(false, ExpenseState())

            ExpenseIntent.ShowAddCategoryDialog -> _showAddCategoryDialog.value = true

            ExpenseIntent.HideAddCategoryDialog -> _showAddCategoryDialog.value = false

            is ExpenseIntent.DeleteCategory -> deleteCategory(intent)
            is ExpenseIntent.UpdateCategory -> updateCategory(intent)
        }
    }

    private fun addCategory(intent: ExpenseIntent.AddCategory) {
        val expenseState = ExpenseState(
            category = intent.title,
            budget = intent.budget,
            spendAmount = 0f
        )

        viewModelScope.launch {
            dashboardRepository.addCategory(expenseState)
        }
    }

    private fun deleteCategory(intent: ExpenseIntent.DeleteCategory) {
        viewModelScope.launch {
            dashboardRepository.deleteCategory(intent.expenseState)
        }
    }

    private fun updateCategory(intent: ExpenseIntent.UpdateCategory) {
        viewModelScope.launch {
            dashboardRepository.updateCategory(intent.expenseState)
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
            }
        }
    }

    private fun spend(intent: ExpenseIntent.Spend) {
        val newValue =
            selectedExpense.copy(spendAmount = selectedExpense.spendAmount + intent.amount)

        viewModelScope.launch {
            dashboardRepository.updateCategory(newValue)
        }
    }
}
