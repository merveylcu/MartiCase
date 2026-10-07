package com.merveylcu.marticase.core.designsystem.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
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
    val colors = MartiCaseTheme.colors
    IconButton(
        onClick = onClick,
        modifier = modifier.size(MartiCaseTheme.dimens.controlHeight),
        enabled = enabled,
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = colors.onSurfaceVariant,
            disabledContentColor = colors.outlineVariant,
        ),
    ) {
        MartiIcon(imageVector = icon, contentDescription = contentDescription)
    }
}
