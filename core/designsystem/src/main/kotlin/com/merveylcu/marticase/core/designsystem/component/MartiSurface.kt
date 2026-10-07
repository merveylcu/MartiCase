package com.merveylcu.marticase.core.designsystem.component

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.merveylcu.marticase.core.designsystem.theme.MartiCaseTheme

@Composable
fun MartiSurface(
    modifier: Modifier = Modifier,
    shape: Shape = MartiCaseTheme.shapes.large,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = MartiCaseTheme.colors.surface,
        contentColor = MartiCaseTheme.colors.onSurface,
        shadowElevation = MartiCaseTheme.dimens.elevation,
        content = content,
    )
}
