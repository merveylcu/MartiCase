package com.merveylcu.marticase.feature.tracking.data.mapper

import com.merveylcu.marticase.core.database.entity.RoutePointEntity
import com.merveylcu.marticase.feature.tracking.domain.model.Coordinate
import com.merveylcu.marticase.feature.tracking.domain.model.LocationFix
import com.merveylcu.marticase.feature.tracking.domain.model.RoutePoint

internal fun RoutePointEntity.toDomain(): RoutePoint = RoutePoint(
    id = id,
    coordinate = Coordinate(latitude, longitude),
    accuracyMeters = accuracyMeters,
    recordedAtMillis = recordedAtMillis,
    address = address,
)

internal fun LocationFix.toEntity(): RoutePointEntity = RoutePointEntity(
    latitude = coordinate.latitude,
    longitude = coordinate.longitude,
    accuracyMeters = accuracyMeters,
    recordedAtMillis = timeMillis,
)
