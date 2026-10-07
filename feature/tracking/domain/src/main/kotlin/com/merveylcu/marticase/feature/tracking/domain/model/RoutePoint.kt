package com.merveylcu.marticase.feature.tracking.domain.model

public data class RoutePoint(
    val id: Long,
    val coordinate: Coordinate,
    val accuracyMeters: Float,
    val recordedAtMillis: Long,
    val address: String?,
)
