package com.merveylcu.marticase.core.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.merveylcu.marticase.core.designsystem.icon.MartiIcons
import com.merveylcu.marticase.core.designsystem.theme.MartiCaseTheme

@Composable
fun MartiZoomControls(
    zoomInDescription: String,
    zoomOutDescription: String,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MartiSurface(modifier = modifier, shape = MartiCaseTheme.shapes.full) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            MartiIconButton(
                icon = MartiIcons.Add,
                contentDescription = zoomInDescription,
                onClick = onZoomIn,
            )
            HorizontalDivider(
                modifier = Modifier.width(MartiCaseTheme.iconSizes.medium),
                color = MartiCaseTheme.colors.outlineVariant,
            )
            MartiIconButton(
                icon = MartiIcons.Remove,
                contentDescription = zoomOutDescription,
                onClick = onZoomOut,
            )
        }
    }
}
