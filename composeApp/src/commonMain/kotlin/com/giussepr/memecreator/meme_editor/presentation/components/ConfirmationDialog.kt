package com.giussepr.memecreator.meme_editor.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

data class ConfirmationDialogConfig(
    val title: String,
    val message: String,
    val confirmButtonText: String,
    val cancelButtonText: String,
    val confirmButtonColor: Color? = null
)

@Composable
fun ConfirmationDialog(
    config: ConfirmationDialogConfig,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text(
                    text = config.confirmButtonText,
                    color = config.confirmButtonColor ?: MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = config.cancelButtonText,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        },
        text = {
            Text(
                text = config.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        title = {
            Text(
                text = config.title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        modifier = modifier
    )
}
