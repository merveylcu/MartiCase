package com.merveylcu.marticase.core.designsystem.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.merveylcu.marticase.core.designsystem.theme.MartiCaseTheme

@Composable
fun MartiIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    FilledTonalIconButton(
        onClick = onClick,
        modifier = modifier.size(MartiCaseTheme.dimens.controlHeight),
        enabled = enabled,
        colors = IconButtonDefaults.filledTonalIconButtonColors(
            containerColor = MartiCaseTheme.colors.surface,
            contentColor = MartiCaseTheme.colors.onSurface,
        ),
    ) {
        MartiIcon(imageVector = icon, contentDescription = contentDescription)
    }
}
