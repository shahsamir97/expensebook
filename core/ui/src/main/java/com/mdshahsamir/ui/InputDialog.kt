package com.mdshahsamir.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mdshahsamir.ui.theme.ExpenseBookTheme

@Composable
fun InputDialog(title: String, onClickSpend: (amount: Float) -> Unit, onClose: () -> Unit) {
    var amount by rememberSaveable { mutableStateOf("") }
    var isError by rememberSaveable { mutableStateOf(false) }

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
                    isError = isError,
                    supportingText = {
                        if (isError) {
                            Text(
                                text = stringResource(R.string.please_enter_a_valid_number),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                    ),
                )
                Spacer(modifier = Modifier.height(6.dp))
                Button(onClick = {
                    try {
                        isError = false
                        onClickSpend(amount.toFloat())
                    } catch (e:Exception) {
                        isError = true
                        e.printStackTrace()
                    }

                }) {
                    Text(text = stringResource(R.string.spend))
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
    var isError by rememberSaveable { mutableStateOf(false) }

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
                    label = { Text(text = stringResource(R.string.enter_title)) },
                    supportingText = { Text(stringResource(R.string.eg_grocery_home_rent_etc)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                    ),
                    singleLine = true
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = budget,
                    onValueChange = { budget = it },
                    label = { Text(stringResource(R.string.enter_budget)) },
                    supportingText = {
                        if (isError) {
                            Text(
                                text = stringResource(R.string.please_enter_a_valid_number),
                                color = MaterialTheme.colorScheme.error
                            )
                        } else {
                            Text(stringResource(R.string.eg_100_200_50))
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                    ),

                )
                Spacer(modifier = Modifier.height(6.dp))
                Button(onClick = {
                    try {
                        onClickAddCategory(title, budget.toFloat())
                        isError = false
                    } catch (e: Exception) {
                        isError = true
                    }
                }
                ) {
                    Text(text = stringResource(R.string.add_category))
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
    var isError by rememberSaveable { mutableStateOf(false) }

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
                    label = { Text(text = stringResource(R.string.enter_title)) },
                    supportingText = { Text(stringResource(R.string.eg_grocery_home_rent_etc)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                    ),
                    singleLine = true
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = budgetInput,
                    onValueChange = { budgetInput = it },
                    label = { Text(stringResource(R.string.enter_budget)) },
                    supportingText = { Text(stringResource(R.string.eg_1000_2800_50)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next,
                    ),
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = spendAmountInput,
                    onValueChange = { spendAmountInput = it },
                    label = { Text(stringResource(R.string.enter_spend_amount)) },
                    supportingText = { Text(stringResource(R.string.eg_200_550_50)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                    ),
                )
                Spacer(modifier = Modifier.height(6.dp))

                if (isError) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.please_enter_a_valid_number),
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }

                Button(onClick = {
                    try {
                        onClickUpdateCategory(
                            titleInput,
                            budgetInput.toFloat(),
                            spendAmountInput.toFloat()
                        )
                        isError = false
                    } catch (e: Exception) {
                        isError = true
                    }
                }
                ) {
                    Text(text = stringResource(R.string.update_category))
                }
            }
        }
    }
}

@Composable
fun InputIncomeDialog(title: String, onClickAdd: (amount: Float) -> Unit, onClose: () -> Unit) {
    var amount by rememberSaveable { mutableStateOf("") }
    var isError by rememberSaveable { mutableStateOf(false) }

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
                    label = { Text(stringResource(R.string.enter_your_income)) },
                    isError = isError,
                    supportingText = {
                        if (isError) {
                            Text(
                                text = stringResource(R.string.please_enter_a_valid_number),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                    ),
                )
                Spacer(modifier = Modifier.height(6.dp))
                Button(onClick = {
                    try {
                        onClickAdd(amount.toFloat())
                        isError = false
                    } catch (e: Exception) {
                        isError = true
                        e.printStackTrace()
                    }
                }
                ) {
                    Text(text = stringResource(R.string.add))
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