package com.merveylcu.marticase.feature.tracking.presentation.model

import androidx.compose.runtime.Immutable
import com.merveylcu.marticase.feature.tracking.domain.model.RoutePoint

@Immutable
data class SelectedPoint(
    val point: RoutePoint,
    val order: Int,
    val address: AddressState,
)
