package com.merveylcu.marticase.feature.tracking.domain.usecase

import com.merveylcu.marticase.feature.tracking.domain.repository.TrackingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

public class ObserveTrackingUseCase
@Inject
constructor(
    private val repository: TrackingRepository,
) {
    public operator fun invoke(): Flow<Boolean> = repository.isTracking
}
