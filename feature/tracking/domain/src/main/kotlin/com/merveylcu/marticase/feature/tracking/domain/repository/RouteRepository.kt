package com.merveylcu.marticase.feature.tracking.domain.repository

import com.merveylcu.marticase.feature.tracking.domain.model.LocationFix
import com.merveylcu.marticase.feature.tracking.domain.model.RoutePoint
import kotlinx.coroutines.flow.Flow

public interface RouteRepository {
    public fun observeRoute(): Flow<List<RoutePoint>>

    public suspend fun lastPoint(): RoutePoint?

    public suspend fun addPoint(fix: LocationFix): RoutePoint

    public suspend fun clear()
}
