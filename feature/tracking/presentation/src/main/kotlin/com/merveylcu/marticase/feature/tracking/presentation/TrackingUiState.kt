package com.merveylcu.marticase.feature.tracking.presentation

import androidx.compose.runtime.Immutable
import com.merveylcu.marticase.feature.tracking.domain.model.RoutePoint
import com.merveylcu.marticase.feature.tracking.presentation.model.SelectedPoint
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class TrackingUiState(
    val points: ImmutableList<RoutePoint> = persistentListOf(),
    val isTracking: Boolean = false,
    val selectedPoint: SelectedPoint? = null,
    val isResetDialogVisible: Boolean = false,
)
