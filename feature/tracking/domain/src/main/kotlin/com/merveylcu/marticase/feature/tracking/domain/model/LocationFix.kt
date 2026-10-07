package com.merveylcu.marticase.feature.tracking.domain.model

/** A raw location update from the device. */
public data class LocationFix(
    val coordinate: Coordinate,
    val accuracyMeters: Float,
    val timeMillis: Long,
)
