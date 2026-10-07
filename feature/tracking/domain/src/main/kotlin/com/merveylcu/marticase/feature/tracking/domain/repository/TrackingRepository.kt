package com.merveylcu.marticase.feature.tracking.domain.repository

import kotlinx.coroutines.flow.Flow

public interface TrackingRepository {
    public val isTracking: Flow<Boolean>

    /** Starts tracking. Returns false if it could not start (no permission, app not visible). */
    public suspend fun startTracking(): Boolean

    public suspend fun stopTracking()
}
