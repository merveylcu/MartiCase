package com.merveylcu.marticase.core.designsystem.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.merveylcu.marticase.core.designsystem.theme.MartiCaseTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MartiBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MartiCaseTheme.colors.surface,
        contentColor = MartiCaseTheme.colors.onSurface,
        content = content,
    )
}
