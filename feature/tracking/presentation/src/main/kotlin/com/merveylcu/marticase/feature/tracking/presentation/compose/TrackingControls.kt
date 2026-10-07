package com.merveylcu.marticase.feature.tracking.presentation.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.merveylcu.marticase.core.designsystem.component.MartiButton
import com.merveylcu.marticase.core.designsystem.component.MartiButtonStyle
import com.merveylcu.marticase.core.designsystem.component.MartiIconButton
import com.merveylcu.marticase.core.designsystem.component.MartiSurface
import com.merveylcu.marticase.core.designsystem.icon.MartiIcons
import com.merveylcu.marticase.core.designsystem.theme.MartiCaseTheme
import com.merveylcu.marticase.feature.tracking.presentation.R

@Composable
internal fun TrackingControls(
    isTracking: Boolean,
    canReset: Boolean,
    onStartClick: () -> Unit,
    onStopClick: () -> Unit,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MartiSurface(modifier = modifier, shape = MartiCaseTheme.shapes.extraLarge) {
        Row(
            modifier = Modifier.padding(MartiCaseTheme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MartiCaseTheme.spacing.xs),
        ) {
            if (isTracking) {
                MartiButton(
                    text = stringResource(R.string.tracking_stop),
                    onClick = onStopClick,
                    icon = MartiIcons.Close,
                    style = MartiButtonStyle.Danger,
                    modifier = Modifier.weight(1f),
                )
            } else {
                MartiButton(
                    text = stringResource(R.string.tracking_start),
                    onClick = onStartClick,
                    icon = MartiIcons.Play,
                    modifier = Modifier.weight(1f),
                )
            }
            MartiIconButton(
                icon = MartiIcons.Delete,
                contentDescription = stringResource(R.string.tracking_reset),
                onClick = onResetClick,
                enabled = canReset,
            )
        }
    }
}
