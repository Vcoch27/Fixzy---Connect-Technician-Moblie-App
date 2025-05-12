package com.example.fixzy_ketnoikythuatvien.ui.components.notifications


import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CustomAlertDialog(
    title: String?,
    message: String,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                if (title != null) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            },
            text = {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("Đóng")
                }
            },
            modifier = modifier
        )
    }
}