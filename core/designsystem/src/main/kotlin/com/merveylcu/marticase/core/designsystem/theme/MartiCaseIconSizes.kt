package com.merveylcu.marticase.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class MartiCaseIconSizes(val medium: Dp = 24.dp, val large: Dp = 48.dp)

internal val LocalIconSizes = staticCompositionLocalOf { MartiCaseIconSizes() }
