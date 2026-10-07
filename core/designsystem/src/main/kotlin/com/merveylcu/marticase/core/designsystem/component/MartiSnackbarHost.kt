package com.merveylcu.marticase.core.designsystem.component

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.merveylcu.marticase.core.designsystem.theme.MartiCaseTheme

@Composable
fun MartiSnackbarHost(hostState: SnackbarHostState, modifier: Modifier = Modifier) {
    SnackbarHost(hostState = hostState, modifier = modifier) { data ->
        Snackbar(
            snackbarData = data,
            shape = MartiCaseTheme.shapes.medium,
            containerColor = MartiCaseTheme.colors.onSurface,
            contentColor = MartiCaseTheme.colors.surface,
        )
    }
}
