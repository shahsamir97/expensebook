package com.mdshahsamir.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mdshahsamir.ui.theme.ExpenseBookTheme

@Composable
fun InputDialog(title: String, onClickSpend: (amount: Float) -> Unit, onClickAddFund: (amount: Float) -> Unit, onClose: () -> Unit) {
    var amount by rememberSaveable { mutableStateOf("") }

    Dialog(onDismissRequest = onClose) {
        Card {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = title, style = MaterialTheme.typography.titleLarge)
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text(stringResource(R.string.enter_amount)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row (horizontalArrangement = Arrangement.Center) {
                    Button(onClick = { onClickSpend(amount.toFloat()) }) {
                        Text(text = "Spend")
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Button(onClick = { onClickAddFund(amount.toFloat()) }) {
                        Text(text = "Add Fund")
                    }
                }
            }
        }
    }
}

@Composable
fun CreateCategoryDialog(
    onClickAddCategory: (title: String, budget: Float) -> Unit,
    onClose: () -> Unit
) {
    var title by rememberSaveable { mutableStateOf("") }
    var budget by rememberSaveable { mutableStateOf("") }

    Dialog(onDismissRequest = onClose) {
        Card {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(text = "Enter Title") },
                    supportingText = { Text("Eg. Grocery, Home Rent, etc.") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = true
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = budget,
                    onValueChange = { budget = it },
                    label = { Text("Enter Budget") },
                    supportingText = { Text("Eg. 100, 200.50") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
                Spacer(modifier = Modifier.height(6.dp))
                Button(onClick = { onClickAddCategory(title, budget.toFloat()) }) {
                    Text(text = "Add Category")
                }
            }
        }
    }
}

@Composable
fun EditCategoryDialog(
    title: String,
    budget: Float,
    spend: Float,
    onClickUpdateCategory: (title: String, budget: Float, spend: Float) -> Unit,
    onClose: () -> Unit
) {
    var titleInput by rememberSaveable { mutableStateOf(title) }
    var budgetInput by rememberSaveable { mutableStateOf(budget.toString()) }
    var spendAmountInput by rememberSaveable { mutableStateOf(spend.toString()) }

    Dialog(onDismissRequest = onClose) {
        Card {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = titleInput,
                    onValueChange = { titleInput = it },
                    label = { Text(text = "Enter Title") },
                    supportingText = { Text("Eg. Grocery, Home Rent, etc.") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = true
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = budgetInput,
                    onValueChange = { budgetInput = it },
                    label = { Text("Enter Budget") },
                    supportingText = { Text("Eg. 1000, 2800.50") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = spendAmountInput,
                    onValueChange = { spendAmountInput = it },
                    label = { Text("Enter Spend Amount") },
                    supportingText = { Text("Eg. 200, 550.50") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
                Spacer(modifier = Modifier.height(6.dp))
                Button(onClick = { onClickUpdateCategory(titleInput, budgetInput.toFloat(), spendAmountInput.toFloat()) }) {
                    Text(text = "Update Category")
                }
            }
        }
    }
}

@Preview
@Composable
fun InputDialogPreview() {
    ExpenseBookTheme {
        InputDialog(
            title = "Miscellaneous",
            onClickSpend = {},
            onClickAddFund = {},
            onClose = {},
        )
    }
}

@Preview
@Composable
fun CreateCategoryDialogPreview() {
    ExpenseBookTheme {
        CreateCategoryDialog(
            onClickAddCategory = { _,_-> },
            onClose = {},
        )
    }
}

@Preview
@Composable
fun EditCategoryDialogPreview() {
    ExpenseBookTheme {
        EditCategoryDialog(
            title = "",
            budget = 0f,
            spend = 0f,
            onClickUpdateCategory = { _, _, _-> },
            onClose = {},
        )
    }
}