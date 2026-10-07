package com.merveylcu.marticase.feature.tracking.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.merveylcu.marticase.feature.tracking.domain.model.Coordinate
import com.merveylcu.marticase.feature.tracking.domain.model.LocationFix
import kotlinx.coroutines.test.runTest
import org.junit.Test

// 0.0009 degrees of latitude is about 100.1 m, 0.0008 is about 89 m.
private const val START_LAT = 41.0
private const val LON = 29.0

class RecordLocationUseCaseTest {
    private val repository = FakeRouteRepository()
    private val recordLocation = RecordLocationUseCase(repository)

    @Test
    fun firstAccurateFix_becomesFirstMarker() = runTest {
        val marker = recordLocation(fix(START_LAT))

        assertThat(marker).isNotNull()
        assertThat(repository.points.value).hasSize(1)
    }

    @Test
    fun fixCloserThan100Meters_isIgnored() = runTest {
        recordLocation(fix(START_LAT))

        val marker = recordLocation(fix(START_LAT + 0.0008))

        assertThat(marker).isNull()
        assertThat(repository.points.value).hasSize(1)
    }

    @Test
    fun fix100MetersFromLastMarker_addsMarker() = runTest {
        recordLocation(fix(START_LAT))

        recordLocation(fix(START_LAT + 0.0009))

        assertThat(repository.points.value).hasSize(2)
    }

    @Test
    fun distanceIsMeasuredFromLastMarker_notFromLastFix() = runTest {
        recordLocation(fix(START_LAT))
        // Two 50 m steps: neither is 100 m from the previous fix,
        // but the second one is 100 m from the marker.
        recordLocation(fix(START_LAT + 0.00045))
        recordLocation(fix(START_LAT + 0.0009))

        assertThat(repository.points.value).hasSize(2)
    }

    @Test
    fun inaccurateFix_isIgnored() = runTest {
        val marker = recordLocation(fix(START_LAT, accuracy = 80f))

        assertThat(marker).isNull()
        assertThat(repository.points.value).isEmpty()
    }

    @Test
    fun fixWithAccuracyAtLimit_isAccepted() = runTest {
        val marker =
            recordLocation(fix(START_LAT, accuracy = RecordLocationUseCase.MAX_ACCURACY_METERS))

        assertThat(marker).isNotNull()
    }

    private fun fix(latitude: Double, accuracy: Float = 5f) = LocationFix(
        coordinate = Coordinate(latitude, LON),
        accuracyMeters = accuracy,
        timeMillis = 0L,
    )
}
