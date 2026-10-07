package com.merveylcu.marticase.feature.tracking.presentation

import androidx.compose.runtime.Immutable
import com.merveylcu.marticase.feature.tracking.domain.model.RoutePoint
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class TrackingUiState(
    val points: ImmutableList<RoutePoint> = persistentListOf(),
    val isTracking: Boolean = false,
    val selectedPoint: SelectedPoint? = null,
    val isResetDialogVisible: Boolean = false,
)

@Immutable
data class SelectedPoint(
    val point: RoutePoint,
    val order: Int,
    val address: AddressState,
)

@Immutable
sealed interface AddressState {
    data object Loading : AddressState

    data class Loaded(val text: String) : AddressState

    data object Unavailable : AddressState
}
