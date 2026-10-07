package com.merveylcu.marticase.feature.tracking.domain.repository

import kotlinx.coroutines.flow.Flow

public interface TrackingRepository {
    public val isTracking: Flow<Boolean>

    public suspend fun setTracking(enabled: Boolean)
}
