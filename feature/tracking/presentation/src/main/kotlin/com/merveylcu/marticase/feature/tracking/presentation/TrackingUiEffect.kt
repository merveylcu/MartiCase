package com.merveylcu.marticase.feature.tracking.presentation

sealed interface TrackingUiEffect {
    data object StartFailed : TrackingUiEffect
}
