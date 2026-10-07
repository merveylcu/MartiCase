package com.merveylcu.marticase.core.designsystem.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.merveylcu.marticase.core.designsystem.theme.MartiCaseTheme

@Composable
fun MartiProgressIndicator(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier.size(MartiCaseTheme.iconSizes.medium),
        color = MartiCaseTheme.colors.primary,
        strokeWidth = MartiCaseTheme.dimens.borderThick,
    )
}
