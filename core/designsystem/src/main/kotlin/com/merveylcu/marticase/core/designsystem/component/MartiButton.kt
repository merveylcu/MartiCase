package com.merveylcu.marticase.core.designsystem.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.merveylcu.marticase.core.designsystem.theme.MartiCaseTheme

@Composable
fun MartiButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    style: MartiButtonStyle = MartiButtonStyle.Primary,
    enabled: Boolean = true,
) {
    val colors = MartiCaseTheme.colors
    Button(
        onClick = onClick,
        modifier = modifier.height(MartiCaseTheme.dimens.controlHeight),
        enabled = enabled,
        shape = MartiCaseTheme.shapes.full,
        colors = when (style) {
            MartiButtonStyle.Primary -> ButtonDefaults.buttonColors(
                containerColor = colors.primary,
                contentColor = colors.onPrimary,
            )

            MartiButtonStyle.Danger -> ButtonDefaults.buttonColors(
                containerColor = colors.error,
                contentColor = colors.onError,
            )
        },
    ) {
        if (icon != null) {
            MartiIcon(imageVector = icon, contentDescription = null)
            Spacer(Modifier.width(MartiCaseTheme.spacing.sm))
        }
        MartiText(text = text, style = MartiCaseTheme.typography.labelLarge)
    }
}
