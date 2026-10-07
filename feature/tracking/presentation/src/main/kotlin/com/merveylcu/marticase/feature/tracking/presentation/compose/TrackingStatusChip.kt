package com.merveylcu.marticase.feature.tracking.presentation.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.merveylcu.marticase.core.designsystem.component.MartiSurface
import com.merveylcu.marticase.core.designsystem.component.MartiText
import com.merveylcu.marticase.core.designsystem.theme.MartiCaseTheme
import com.merveylcu.marticase.feature.tracking.presentation.R

@Composable
internal fun TrackingStatusChip(
    isTracking: Boolean,
    markerCount: Int,
    modifier: Modifier = Modifier,
) {
    val spacing = MartiCaseTheme.spacing
    val colors = MartiCaseTheme.colors
    val dotColor = if (isTracking) colors.primary else colors.outline
    val status = if (isTracking) {
        R.string.tracking_status_active
    } else {
        R.string.tracking_status_paused
    }
    MartiSurface(modifier = modifier, shape = MartiCaseTheme.shapes.full) {
        Row(
            modifier = Modifier.padding(horizontal = spacing.md, vertical = spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacing.sm),
        ) {
            Box(
                Modifier
                    .size(MartiCaseTheme.dimens.statusDot)
                    .background(color = dotColor, shape = MartiCaseTheme.shapes.full),
            )
            MartiText(
                text = stringResource(status),
                style = MartiCaseTheme.typography.labelLarge,
            )
            MartiText(
                text = pluralStringResource(
                    R.plurals.tracking_marker_count,
                    markerCount,
                    markerCount,
                ),
                style = MartiCaseTheme.typography.bodyMedium,
                color = MartiCaseTheme.colors.onSurfaceVariant,
            )
        }
    }
}
