package com.merveylcu.marticase.feature.tracking.domain.model

public data class LocationFix(
    val coordinate: Coordinate,
    val accuracyMeters: Float,
    val timeMillis: Long,
)
