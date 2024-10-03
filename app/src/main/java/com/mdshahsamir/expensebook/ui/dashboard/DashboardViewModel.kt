package com.mdshahsamir.expensebook.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdshahsamir.expensebook.intent.ExpenseIntent
import com.mdshahsamir.expensebook.model.ExpenseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository
) : ViewModel() {
    private val listOfExpenses = ArrayList<ExpenseState>()

    private val _listOfExpenseState = MutableStateFlow(listOfExpenses)
    val listOfExpenseState: StateFlow<ArrayList<ExpenseState>> = _listOfExpenseState

    private val _showInputDialogState = MutableStateFlow(Pair(false, ExpenseState()))
    val showInputDialogState: StateFlow<Pair<Boolean, ExpenseState>> = _showInputDialogState

    private val _showAddCategoryDialog = MutableStateFlow(false)
    val showAddCategoryDialog: StateFlow<Boolean> = _showAddCategoryDialog

    private var selectedExpenseStateIndex = -1

    init {
        listOfExpenses.add(ExpenseState("Miscellaneous", 1000f, 700f))
    }

    fun processIntent(intent: ExpenseIntent) {
        when(intent) {
            is ExpenseIntent.Spend -> spend(intent)
            is ExpenseIntent.AddFund -> addFund(intent)
            is ExpenseIntent.AddCategory -> addCategory(intent)

            is ExpenseIntent.ShowInputDialog -> {
                selectedExpenseStateIndex = intent.expenseIndex
                _showInputDialogState.value = Pair(true, listOfExpenses[intent.expenseIndex])
            }

            ExpenseIntent.HideInputDialog -> _showInputDialogState.value = Pair(false, ExpenseState())

            ExpenseIntent.ShowAddCategoryDialog -> _showAddCategoryDialog.value = true

            ExpenseIntent.HideAddCategoryDialog -> _showAddCategoryDialog.value = false
        }
    }

    private fun addCategory(intent: ExpenseIntent.AddCategory) {
        val expense = ExpenseState(
            category = intent.title,
            budget = intent.budget,
            spendAmount = 0f
        )

        viewModelScope.launch {
            dashboardRepository.addCategory(expense)
        }
    }

    private fun addFund(intent: ExpenseIntent.AddFund) {
        val expense = listOfExpenses[selectedExpenseStateIndex]
        val newBudget = expense.budget + intent.amount
        val newValue = expense.copy(
            budget = newBudget,
            spendAmount = expense.spendAmount.let { if (it < 0) it.plus(intent.amount) else it }
        )

        listOfExpenses[selectedExpenseStateIndex] = newValue
        _listOfExpenseState.value = listOfExpenses
    }

    private fun spend(intent: ExpenseIntent.Spend) {
        val expense = listOfExpenses[selectedExpenseStateIndex]
        val newValue = expense.copy(spendAmount = expense.spendAmount + intent.amount)

        listOfExpenses[selectedExpenseStateIndex] = newValue
        _listOfExpenseState.value = listOfExpenses
    }
}
