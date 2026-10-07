package com.merveylcu.marticase.feature.tracking.domain.usecase

import com.merveylcu.marticase.feature.tracking.domain.model.LocationFix
import com.merveylcu.marticase.feature.tracking.domain.model.RoutePoint
import com.merveylcu.marticase.feature.tracking.domain.repository.RouteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeRouteRepository : RouteRepository {
    val points = MutableStateFlow<List<RoutePoint>>(emptyList())

    override fun observeRoute(): Flow<List<RoutePoint>> = points

    override suspend fun lastPoint(): RoutePoint? = points.value.lastOrNull()

    override suspend fun addPoint(fix: LocationFix): RoutePoint {
        val point = RoutePoint(
            id = points.value.size + 1L,
            coordinate = fix.coordinate,
            accuracyMeters = fix.accuracyMeters,
            recordedAtMillis = fix.timeMillis,
            address = null,
        )
        points.value += point
        return point
    }

    override suspend fun clear() {
        points.value = emptyList()
    }
}
