package com.mdshahsamir.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun EbAlertDialog(
    title: String,
    bodyText: String,
    onClickConfirm: () -> Unit,
    onClickDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onClickDismiss,
        confirmButton = { TextButton(onClick = onClickConfirm){ Text(text = stringResource(R.string.yes)) } },
        title = { Text(text = title) },
        text = { Text(text = bodyText) },
        dismissButton = { TextButton(onClick = onClickDismiss) { Text(text = stringResource(R.string.no)) } },
    )
}