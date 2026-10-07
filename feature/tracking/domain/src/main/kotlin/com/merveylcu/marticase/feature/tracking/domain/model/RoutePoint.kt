package com.merveylcu.marticase.feature.tracking.domain.model

/** A marker on the route. [address] is filled in lazily, the first time it is requested. */
public data class RoutePoint(
    val id: Long,
    val coordinate: Coordinate,
    val accuracyMeters: Float,
    val recordedAtMillis: Long,
    val address: String?,
)
