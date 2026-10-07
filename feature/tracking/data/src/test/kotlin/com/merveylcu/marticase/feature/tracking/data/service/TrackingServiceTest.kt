package com.merveylcu.marticase.feature.tracking.data.service

import android.app.Service
import android.content.Intent
import android.database.sqlite.SQLiteException
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.merveylcu.marticase.core.common.di.CoroutinesModule
import com.merveylcu.marticase.core.common.dispatcher.ApplicationScope
import com.merveylcu.marticase.core.common.dispatcher.DefaultDispatcher
import com.merveylcu.marticase.core.common.dispatcher.IoDispatcher
import com.merveylcu.marticase.feature.tracking.data.di.TrackingDataModule
import com.merveylcu.marticase.feature.tracking.data.location.LocationClient
import com.merveylcu.marticase.feature.tracking.domain.model.Coordinate
import com.merveylcu.marticase.feature.tracking.domain.model.LocationFix
import com.merveylcu.marticase.feature.tracking.domain.model.RoutePoint
import com.merveylcu.marticase.feature.tracking.domain.repository.AddressRepository
import com.merveylcu.marticase.feature.tracking.domain.repository.RouteRepository
import com.merveylcu.marticase.feature.tracking.domain.repository.TrackingRepository
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@UninstallModules(TrackingDataModule::class, CoroutinesModule::class)
@Config(application = HiltTestApplication::class)
@RunWith(AndroidJUnit4::class)
class TrackingServiceTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private val testDispatcher = UnconfinedTestDispatcher()
    private val isTracking = MutableStateFlow(true)
    private val fixes = MutableSharedFlow<LocationFix>()

    @BindValue
    @JvmField
    @DefaultDispatcher
    val defaultDispatcher: CoroutineDispatcher = testDispatcher

    @BindValue
    @JvmField
    @IoDispatcher
    val ioDispatcher: CoroutineDispatcher = testDispatcher

    @BindValue
    @JvmField
    @ApplicationScope
    val applicationScope: CoroutineScope = CoroutineScope(testDispatcher)

    @BindValue
    @JvmField
    internal val locationClient: LocationClient = mockk()

    @BindValue
    @JvmField
    val routeRepository: RouteRepository = mockk()

    @BindValue
    @JvmField
    val trackingRepository: TrackingRepository = mockk()

    @BindValue
    @JvmField
    val addressRepository: AddressRepository = mockk()

    @Before
    fun setUp() {
        every { locationClient.hasPermission() } returns true
        every { locationClient.locationUpdates() } returns fixes
        every { trackingRepository.isTracking } returns isTracking
        coEvery { trackingRepository.stopTracking() } answers { isTracking.value = false }
        coEvery { routeRepository.lastPoint() } returns null
        coEvery { routeRepository.addPoint(any()) } answers { firstArg<LocationFix>().toPoint() }
    }

    @Test
    fun start_runsInForegroundAndRecordsMarkers() = runTest {
        val service = createService()

        val result = service.onStartCommand(startIntent(), 0, 1)
        fixes.emit(fix(41.000))
        fixes.emit(fix(41.001))

        assertThat(result).isEqualTo(Service.START_STICKY)
        assertThat(shadowOf(service).lastForegroundNotification).isNotNull()
        coVerify(exactly = 2) { routeRepository.addPoint(any()) }
    }

    @Test
    fun start_withoutPermission_stopsAndTurnsTrackingOff() {
        every { locationClient.hasPermission() } returns false
        val service = createService()

        val result = service.onStartCommand(startIntent(), 0, 1)

        assertThat(result).isEqualTo(Service.START_NOT_STICKY)
        assertThat(isTracking.value).isFalse()
        assertThat(shadowOf(service).isStoppedBySelf).isTrue()
        verify(exactly = 0) { locationClient.locationUpdates() }
    }

    @Test
    fun stopAction_stopsServiceAndTurnsTrackingOff() {
        val service = createService()
        service.onStartCommand(startIntent(), 0, 1)

        val result = service.onStartCommand(stopIntent(), 0, 2)

        assertThat(result).isEqualTo(Service.START_NOT_STICKY)
        assertThat(isTracking.value).isFalse()
        assertThat(shadowOf(service).isStoppedBySelf).isTrue()
    }

    @Test
    fun stickyRestart_whenTrackingIsOff_stopsWithoutLocationUpdates() {
        isTracking.value = false
        val service = createService()

        val result = service.onStartCommand(null, 0, 1)

        assertThat(result).isEqualTo(Service.START_STICKY)
        assertThat(shadowOf(service).isStoppedBySelf).isTrue()
        verify(exactly = 0) { locationClient.locationUpdates() }
    }

    @Test
    fun stickyRestart_whenTrackingIsOn_resumesRecording() = runTest {
        val service = createService()

        service.onStartCommand(null, 0, 1)
        fixes.emit(fix(41.000))

        assertThat(shadowOf(service).isStoppedBySelf).isFalse()
        coVerify(exactly = 1) { routeRepository.addPoint(any()) }
    }

    @Test
    fun locationError_turnsTrackingOff() {
        every { locationClient.locationUpdates() } returns flow { throw SecurityException() }
        val service = createService()

        service.onStartCommand(startIntent(), 0, 1)

        assertThat(isTracking.value).isFalse()
        assertThat(shadowOf(service).isStoppedBySelf).isTrue()
    }

    @Test
    fun databaseError_skipsFixAndKeepsTracking() = runTest {
        coEvery { routeRepository.addPoint(any()) } throws SQLiteException() andThenAnswer {
            firstArg<LocationFix>().toPoint()
        }
        val service = createService()

        service.onStartCommand(startIntent(), 0, 1)
        fixes.emit(fix(41.000))
        fixes.emit(fix(41.001))

        assertThat(isTracking.value).isTrue()
        assertThat(shadowOf(service).isStoppedBySelf).isFalse()
        coVerify(exactly = 2) { routeRepository.addPoint(any()) }
    }

    private fun createService(): TrackingService =
        Robolectric.buildService(TrackingService::class.java).create().get()

    private fun startIntent() =
        Intent(ApplicationProvider.getApplicationContext(), TrackingService::class.java)

    private fun stopIntent() = startIntent().setAction(TrackingService.ACTION_STOP)

    private fun fix(latitude: Double) = LocationFix(
        coordinate = Coordinate(latitude, 29.0),
        accuracyMeters = 5f,
        timeMillis = 0L,
    )

    private fun LocationFix.toPoint() = RoutePoint(
        id = 1L,
        coordinate = coordinate,
        accuracyMeters = accuracyMeters,
        recordedAtMillis = timeMillis,
        address = null,
    )
}
