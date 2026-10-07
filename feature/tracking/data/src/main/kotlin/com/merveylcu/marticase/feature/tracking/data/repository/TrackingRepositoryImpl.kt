package com.merveylcu.marticase.feature.tracking.data.repository

import com.merveylcu.marticase.core.datastore.TrackingPreferences
import com.merveylcu.marticase.feature.tracking.data.service.TrackingServiceController
import com.merveylcu.marticase.feature.tracking.domain.repository.TrackingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class TrackingRepositoryImpl @Inject constructor(
    private val preferences: TrackingPreferences,
    private val serviceController: TrackingServiceController,
) : TrackingRepository {
    override val isTracking: Flow<Boolean> = preferences.isTracking

    override suspend fun startTracking(): Boolean {
        val started = serviceController.start()
        preferences.setTracking(started)
        return started
    }

    override suspend fun stopTracking() {
        preferences.setTracking(false)
        serviceController.stop()
    }
}
