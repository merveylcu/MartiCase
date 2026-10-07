package com.merveylcu.marticase.feature.tracking.data.repository

import com.google.common.truth.Truth.assertThat
import com.merveylcu.marticase.core.datastore.TrackingPreferences
import com.merveylcu.marticase.feature.tracking.data.service.TrackingServiceController
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class TrackingRepositoryImplTest {
    private val preferences = mockk<TrackingPreferences>(relaxed = true)
    private val controller = mockk<TrackingServiceController>(relaxed = true)
    private val repository = TrackingRepositoryImpl(preferences, controller)

    @Test
    fun start_whenServiceStarts_persistsTrackingOn() = runTest {
        every { controller.start() } returns true

        assertThat(repository.startTracking()).isTrue()
        coVerify { preferences.setTracking(true) }
    }

    @Test
    fun start_whenServiceCannotStart_keepsTrackingOff() = runTest {
        every { controller.start() } returns false

        assertThat(repository.startTracking()).isFalse()
        coVerify { preferences.setTracking(false) }
        coVerify(exactly = 0) { preferences.setTracking(true) }
    }

    @Test
    fun stop_persistsFlagThenStopsService() = runTest {
        repository.stopTracking()

        coVerifyOrder {
            preferences.setTracking(false)
            controller.stop()
        }
    }
}
