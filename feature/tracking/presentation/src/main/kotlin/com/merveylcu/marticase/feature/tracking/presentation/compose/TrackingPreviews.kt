package com.merveylcu.marticase.feature.tracking.presentation.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.merveylcu.marticase.core.designsystem.component.MartiSurface
import com.merveylcu.marticase.core.designsystem.theme.MartiCaseTheme
import com.merveylcu.marticase.feature.tracking.domain.model.Coordinate
import com.merveylcu.marticase.feature.tracking.domain.model.RoutePoint
import com.merveylcu.marticase.feature.tracking.presentation.AddressState
import com.merveylcu.marticase.feature.tracking.presentation.SelectedPoint

private val PreviewPoint = RoutePoint(
    id = 3L,
    coordinate = Coordinate(40.98712, 29.02561),
    accuracyMeters = 4f,
    recordedAtMillis = 0L,
    address = null,
)

@Preview
@Composable
private fun TrackingControlsPreview() {
    MartiCaseTheme {
        TrackingControls(
            isTracking = true,
            canReset = true,
            onStartClick = {},
            onStopClick = {},
            onResetClick = {},
            modifier = Modifier.padding(MartiCaseTheme.spacing.md),
        )
    }
}

@Preview
@Composable
private fun TrackingStatusChipPreview() {
    MartiCaseTheme {
        TrackingStatusChip(isTracking = true, markerCount = 12)
    }
}

@Preview
@Composable
private fun PointDetailContentPreview() {
    MartiCaseTheme {
        MartiSurface {
            PointDetailContent(
                selected = SelectedPoint(
                    point = PreviewPoint,
                    order = 3,
                    address = AddressState.Loaded(
                        "Caferağa, Moda Cd. No:12, 34710 Kadıköy/İstanbul",
                    ),
                ),
                modifier = Modifier.padding(top = MartiCaseTheme.spacing.lg),
            )
        }
    }
}
