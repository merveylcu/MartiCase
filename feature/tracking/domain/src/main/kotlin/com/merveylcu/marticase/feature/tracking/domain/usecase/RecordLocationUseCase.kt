package com.merveylcu.marticase.feature.tracking.domain.usecase

import com.merveylcu.marticase.feature.tracking.domain.model.LocationFix
import com.merveylcu.marticase.feature.tracking.domain.model.RoutePoint
import com.merveylcu.marticase.feature.tracking.domain.repository.RouteRepository
import javax.inject.Inject

public class RecordLocationUseCase
@Inject
constructor(private val repository: RouteRepository) {
    public suspend operator fun invoke(fix: LocationFix): RoutePoint? {
        if (fix.accuracyMeters > MAX_ACCURACY_METERS) return null

        val last = repository.lastPoint()
        val isFarEnough = last == null ||
            last.coordinate.distanceTo(fix.coordinate) >= MARKER_DISTANCE_METERS

        return if (isFarEnough) repository.addPoint(fix) else null
    }

    public companion object {
        public const val MARKER_DISTANCE_METERS: Double = 100.0
        public const val MAX_ACCURACY_METERS: Float = 50f
    }
}
