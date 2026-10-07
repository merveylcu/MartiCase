package com.merveylcu.marticase.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember

@Composable
fun MartiCaseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    spacing: MartiCaseSpacing = MartiCaseSpacing(),
    dimens: MartiCaseDimens = MartiCaseDimens(),
    typography: MartiCaseTypography = DefaultTypography,
    shapes: MartiCaseShapes = MartiCaseShapes(),
    iconSizes: MartiCaseIconSizes = MartiCaseIconSizes(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) DarkColors else LightColors
    val colorScheme = remember(colors) { colors.toColorScheme() }
    val materialTypography = remember(typography) { typography.toTypography() }
    val materialShapes = remember(shapes) { shapes.toShapes() }

    CompositionLocalProvider(
        LocalColors provides colors,
        LocalSpacing provides spacing,
        LocalDimens provides dimens,
        LocalTypography provides typography,
        LocalShapes provides shapes,
        LocalIconSizes provides iconSizes,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = materialTypography,
            shapes = materialShapes,
            content = content,
        )
    }
}

object MartiCaseTheme {
    val colors: MartiCaseColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    val spacing: MartiCaseSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalSpacing.current

    val dimens: MartiCaseDimens
        @Composable
        @ReadOnlyComposable
        get() = LocalDimens.current

    val typography: MartiCaseTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current

    val shapes: MartiCaseShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalShapes.current

    val iconSizes: MartiCaseIconSizes
        @Composable
        @ReadOnlyComposable
        get() = LocalIconSizes.current
}
