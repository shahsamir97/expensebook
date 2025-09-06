package com.mdshahsamir.expensebook.ui.dashboard

import android.content.ContentValues
import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdshahsamir.expensebook.getStartDateOfLastDays
import com.mdshahsamir.expensebook.intent.ExpenseIntent
import com.mdshahsamir.expensebook.isTimeWithinRange
import com.mdshahsamir.expensebook.model.Expense
import com.mdshahsamir.expensebook.model.TransactionData
import com.mdshahsamir.expensebook.model.TransactionFilter
import com.mdshahsamir.expensebook.model.TransactionMode
import com.mdshahsamir.expensebook.toTimestamp
import com.mdshahsamir.expensebook.toUiDateFormat
import com.mdshahsamir.expensebook.ui.transactions.TransactionEvents
import com.mdshahsamir.expensebook.ui.transactions.TransactionsState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository,
    @ApplicationContext private val context: Context,
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
                        _transactionState.value.selectedFilter.let { filter ->
                            isTimeWithinRange(filter.startDate, filter.endDate, it.time.toTimestamp())
                        }
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
        val startDate = getStartDateOfLastDays(filter)
        val endDate = Calendar.getInstance().timeInMillis

        if (startDate == transactionState.value.selectedFilter.startDate) {
            retrieveTransactions()
            _transactionState.update {
                it.copy(
                    selectedFilter = TransactionsState.DefaultState.selectedFilter,
                    showCustomFilter = false,
                )
            }
        } else {
            retrieveTransactions()
            _transactionState.update {
                it.copy(
                    selectedFilter = TransactionFilter(startDate, endDate),
                    showCustomFilter = false,
                )
            }
        }
    }

    override fun clearAllTransaction() {
        viewModelScope.launch {
            dashboardRepository.deleteTransaction(transactionState.value.list)
        }
    }

    override fun onDateRangeSelected(transactionFilter: TransactionFilter) {
        if (transactionFilter.startDate == transactionState.value.selectedFilter.startDate
            && transactionFilter.endDate == transactionState.value.selectedFilter.endDate )
        {
            retrieveTransactions()
            _transactionState.update {
                it.copy(
                    selectedFilter = TransactionsState.DefaultState.selectedFilter,
                    showCustomFilter = false
                )
            }
        } else {
            retrieveTransactions()
            _transactionState.update {
                it.copy(
                    selectedFilter = TransactionFilter(transactionFilter.startDate, transactionFilter.endDate),
                    showCustomFilter = true
                )
            }
        }
    }

    override fun onClickExportPDF() {
        val startDate = if(_transactionState.value.selectedFilter.startDate != Long.MIN_VALUE) {
            _transactionState.value.selectedFilter.startDate.toUiDateFormat()
        } else {
            _transactionState.value.list.first().time
        }
        val endDate = if(_transactionState.value.selectedFilter.endDate != Long.MAX_VALUE) {
            _transactionState.value.selectedFilter.endDate.toUiDateFormat()
        } else {
            _transactionState.value.list.last().time
        }

        val time  = Calendar.getInstance().timeInMillis
        val fileName = "Expense Book Statement $time.pdf"
        val fileContent = generatePdfContent(startDate, endDate)
        val savedPath = savePdfFile(context, fileName, fileContent)

        if (savedPath != null) {
            _transactionState.update { it.copy(showToastMessage = "Pdf save successfully at: $savedPath") }
            Log.d(TAG, "Pdf save successfully at: $savedPath")
        } else {
            _transactionState.update { it.copy(showToastMessage = "Failed to save pdf file") }
            Log.d(TAG, "Failed to save pdf file")
        }
    }

    override fun resetToastMessage() {
        _transactionState.update { it.copy(showToastMessage = "") }
    }

    private fun savePdfFile(context: Context, fileName:String, fileContent: ByteArray): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            savePdfUsingMediaStore(context, fileName, fileContent)
        } else {
            savePdfUsingLegacyStorage(context, fileName, fileContent)
        }
    }

    private fun savePdfUsingMediaStore(context: Context, fileName: String, fileContent: ByteArray): String? {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/Expense_Book")
        }

        val uri = context.contentResolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
        uri?.let {
            context.contentResolver.openOutputStream(it)?.use { outputStream ->
                outputStream.write(fileContent)
                outputStream.flush()
                outputStream.close()
            }
        }

        return "/Document/Expense_Book/$fileName"
    }

    fun generatePdfContent(startDate: String, endDate: String): ByteArray {
        val pdfDocument = PdfDocument()
        var currentPage = createNewPage(pdfDocument)  // Initialize the first page
        var currentY = 60f

        val paint = Paint()
        val textSize = 14f
        val lineHeight = 20f
        val startX = 40f

        // Title
        paint.textSize = 18f
        currentPage.canvas.drawText("Expense Book Statement", startX, currentY, paint)
        currentY += lineHeight * 2

        // Statement Dates
        paint.textSize = textSize
        currentPage.canvas.drawText("Statement Period: $startDate to $endDate", startX, currentY, paint)
        currentY += lineHeight * 2

        // Table Header
        currentPage.canvas.drawText("Category", startX, currentY, paint)
        currentPage.canvas.drawText("Date", startX + 200, currentY, paint)
        currentPage.canvas.drawText("Amount", startX + 400, currentY, paint)
        currentY += lineHeight

        // Table Rows
        var totalSpend = 0f
        _transactionState.value.list.forEach {
            // Check if we have enough space for the next row
            if (currentY > 800) {  // Exceeds page size, so create a new page
                pdfDocument.finishPage(currentPage)
                currentPage = createNewPage(pdfDocument)  // Start a new page
                currentY = 60f

                // Re-draw table header on new page
                currentPage.canvas.drawText("Category", startX, currentY, paint)
                currentPage.canvas.drawText("Date", startX + 200, currentY, paint)
                currentPage.canvas.drawText("Amount", startX + 400, currentY, paint)
                currentY += lineHeight
            }

            // Draw table content
            currentPage.canvas.drawText(it.category, startX, currentY, paint)
            currentPage.canvas.drawText(it.time, startX + 200, currentY, paint)
            currentPage.canvas.drawText(it.amount.toString(), startX + 400, currentY, paint)
            totalSpend += it.amount
            currentY += lineHeight
        }

        // Draw a line before total
        currentY += lineHeight
        currentPage.canvas.drawLine(startX, currentY, startX + 500, currentY, paint)
        currentY += lineHeight

        // Check if Total Spend fits within the current page
        if (currentY + lineHeight * 2 > 800) { // If it exceeds the page limit, start a new page
            pdfDocument.finishPage(currentPage)
            currentPage = createNewPage(pdfDocument)  // Start a new page for total
            currentY = 60f
        }

        // Draw the Total Spend
        paint.textSize = 16f
        currentPage.canvas.drawText("Total Spend: ${totalSpend}", startX + 100, currentY, paint)

        pdfDocument.finishPage(currentPage)

        // Write the PDF content to a ByteArrayOutputStream
        val outputStream = ByteArrayOutputStream()
        pdfDocument.writeTo(outputStream)
        pdfDocument.close()

        return outputStream.toByteArray() // Return the valid PDF content
    }

    fun createNewPage(pdfDocument: PdfDocument): PdfDocument.Page {
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, pdfDocument.pages.size + 1).create()
        return pdfDocument.startPage(pageInfo)
    }

    // For Android 9 and Below (Legacy File System)
    private fun savePdfUsingLegacyStorage(context: Context, fileName: String, fileContent: ByteArray): String? {
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS + "/MyApp")
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val file = File(directory, fileName)
        return try {
            FileOutputStream(file).use { outputStream ->
                outputStream.write(fileContent)
                outputStream.flush()
            }
            file.absolutePath // Return the file path of the saved file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun saveIncomeInput(amount: Float) {
        viewModelScope.launch {
            dashboardRepository.setIncome(amount)

            val income  = dashboardRepository.getIncomeAmount()
            _dashboardState.update { it.copy(income = income) }
        }
    }

    override fun onClickResetCategory() {
        _dashboardState.update { it.copy(showResetBudgetDialog = !_dashboardState.value.showResetBudgetDialog) }
    }

    override fun onConfirmResetCategory() {
        _dashboardState.update { it.copy(showResetBudgetDialog = false) }

        viewModelScope.launch {
            dashboardRepository.resetAllCategory()
        }
    }

    companion object {
        const val TAG = "DashboardViewModel"
    }
}
