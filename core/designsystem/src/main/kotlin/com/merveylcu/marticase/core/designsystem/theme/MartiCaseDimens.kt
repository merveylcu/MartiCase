package com.merveylcu.marticase.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class MartiCaseDimens(
    val controlHeight: Dp = 56.dp,
    val elevation: Dp = 6.dp,
    val borderThick: Dp = 2.dp,
    val statusDot: Dp = 8.dp,
)

internal val LocalDimens = staticCompositionLocalOf { MartiCaseDimens() }
