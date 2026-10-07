package com.merveylcu.marticase.core.designsystem.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.merveylcu.marticase.core.designsystem.theme.MartiCaseTheme

@Composable
fun MartiDialog(
    title: String,
    message: String,
    confirmText: String,
    dismissText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        title = { MartiText(text = title, style = MartiCaseTheme.typography.titleLarge) },
        text = { MartiText(text = message, style = MartiCaseTheme.typography.bodyMedium) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                MartiText(text = confirmText, color = MartiCaseTheme.colors.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { MartiText(text = dismissText) }
        },
        containerColor = MartiCaseTheme.colors.surface,
    )
}
