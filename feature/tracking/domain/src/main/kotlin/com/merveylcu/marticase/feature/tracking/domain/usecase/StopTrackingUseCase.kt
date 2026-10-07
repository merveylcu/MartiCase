package com.merveylcu.marticase.feature.tracking.domain.usecase

import com.merveylcu.marticase.feature.tracking.domain.repository.TrackingRepository
import javax.inject.Inject

public class StopTrackingUseCase @Inject constructor(private val repository: TrackingRepository) {
    public suspend operator fun invoke() {
        repository.stopTracking()
    }
}
