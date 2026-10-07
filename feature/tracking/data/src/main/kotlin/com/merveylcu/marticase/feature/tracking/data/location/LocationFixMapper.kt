package com.merveylcu.marticase.feature.tracking.data.location

import android.location.Location
import com.merveylcu.marticase.feature.tracking.domain.model.Coordinate
import com.merveylcu.marticase.feature.tracking.domain.model.LocationFix

internal const val MAX_FIX_AGE_NANOS = 120_000_000_000L

internal fun Location.toLocationFixOrNull(nowElapsedNanos: Long): LocationFix? {
    if (nowElapsedNanos - elapsedRealtimeNanos > MAX_FIX_AGE_NANOS) return null
    return LocationFix(
        coordinate = Coordinate(latitude, longitude),
        accuracyMeters = if (hasAccuracy()) accuracy else Float.MAX_VALUE,
        timeMillis = time,
    )
}
