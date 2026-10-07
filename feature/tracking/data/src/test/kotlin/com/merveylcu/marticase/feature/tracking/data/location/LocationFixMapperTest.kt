package com.merveylcu.marticase.feature.tracking.data.location

import android.location.Location
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.merveylcu.marticase.feature.tracking.domain.model.Coordinate
import org.junit.Test
import org.junit.runner.RunWith

private const val NOW_NANOS = 100_000_000_000L
private const val ONE_SECOND_NANOS = 1_000_000_000L

@RunWith(AndroidJUnit4::class)
class LocationFixMapperTest {
    @Test
    fun freshFix_isMapped() {
        val fix = location(
            ageNanos = ONE_SECOND_NANOS,
            accuracy = 6f,
        ).toLocationFixOrNull(NOW_NANOS)

        assertThat(fix?.coordinate).isEqualTo(Coordinate(41.0, 29.0))
        assertThat(fix?.accuracyMeters).isEqualTo(6f)
        assertThat(fix?.timeMillis).isEqualTo(1_000L)
    }

    @Test
    fun fixAtMaxAge_isKept() {
        val fix = location(ageNanos = MAX_FIX_AGE_NANOS).toLocationFixOrNull(NOW_NANOS)

        assertThat(fix).isNotNull()
    }

    @Test
    fun staleFix_isDropped() {
        val fix = location(ageNanos = MAX_FIX_AGE_NANOS + 1).toLocationFixOrNull(NOW_NANOS)

        assertThat(fix).isNull()
    }

    @Test
    fun fixWithoutAccuracy_getsWorstAccuracy() {
        val fix = location(
            ageNanos = ONE_SECOND_NANOS,
            accuracy = null,
        ).toLocationFixOrNull(NOW_NANOS)

        assertThat(fix?.accuracyMeters).isEqualTo(Float.MAX_VALUE)
    }

    private fun location(ageNanos: Long, accuracy: Float? = 5f) = Location("test").apply {
        latitude = 41.0
        longitude = 29.0
        time = 1_000L
        elapsedRealtimeNanos = NOW_NANOS - ageNanos
        accuracy?.let { this.accuracy = it }
    }
}
