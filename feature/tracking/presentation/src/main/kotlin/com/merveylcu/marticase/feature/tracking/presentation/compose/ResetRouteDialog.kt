package com.merveylcu.marticase.feature.tracking.presentation.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.merveylcu.marticase.core.designsystem.component.MartiDialog
import com.merveylcu.marticase.feature.tracking.presentation.R

@Composable
internal fun ResetRouteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MartiDialog(
        title = stringResource(R.string.reset_dialog_title),
        message = stringResource(R.string.reset_dialog_message),
        confirmText = stringResource(R.string.reset_dialog_confirm),
        dismissText = stringResource(R.string.reset_dialog_dismiss),
        onConfirm = onConfirm,
        onDismiss = onDismiss,
        modifier = modifier,
    )
}
