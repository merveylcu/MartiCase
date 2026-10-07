package com.merveylcu.marticase.feature.tracking.presentation.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.merveylcu.marticase.core.designsystem.component.MartiButton
import com.merveylcu.marticase.core.designsystem.component.MartiButtonStyle
import com.merveylcu.marticase.core.designsystem.component.MartiIconButton
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
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MartiCaseTheme.spacing.md),
    ) {
        if (isTracking) {
            MartiButton(
                text = stringResource(R.string.tracking_stop),
                onClick = onStopClick,
                icon = Icons.Filled.Close,
                style = MartiButtonStyle.Danger,
                modifier = Modifier.weight(1f),
            )
        } else {
            MartiButton(
                text = stringResource(R.string.tracking_start),
                onClick = onStartClick,
                icon = Icons.Filled.PlayArrow,
                modifier = Modifier.weight(1f),
            )
        }
        MartiIconButton(
            icon = Icons.Filled.Refresh,
            contentDescription = stringResource(R.string.tracking_reset),
            onClick = onResetClick,
            enabled = canReset,
        )
    }
}
