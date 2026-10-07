package com.merveylcu.marticase.feature.tracking.presentation

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.merveylcu.marticase.core.testing.MainDispatcherRule
import com.merveylcu.marticase.feature.tracking.domain.model.Coordinate
import com.merveylcu.marticase.feature.tracking.domain.model.LocationFix
import com.merveylcu.marticase.feature.tracking.domain.model.RoutePoint
import com.merveylcu.marticase.feature.tracking.domain.repository.AddressRepository
import com.merveylcu.marticase.feature.tracking.domain.repository.RouteRepository
import com.merveylcu.marticase.feature.tracking.domain.repository.TrackingRepository
import com.merveylcu.marticase.feature.tracking.domain.usecase.GetAddressUseCase
import com.merveylcu.marticase.feature.tracking.domain.usecase.ObserveRouteUseCase
import com.merveylcu.marticase.feature.tracking.domain.usecase.ObserveTrackingUseCase
import com.merveylcu.marticase.feature.tracking.domain.usecase.ResetRouteUseCase
import com.merveylcu.marticase.feature.tracking.domain.usecase.StartTrackingUseCase
import com.merveylcu.marticase.feature.tracking.domain.usecase.StopTrackingUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class TrackingViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val routeRepository = FakeRouteRepository()
    private val trackingRepository = FakeTrackingRepository()
    private val addressRepository = FakeAddressRepository()

    private val viewModel by lazy {
        TrackingViewModel(
            observeRoute = ObserveRouteUseCase(routeRepository),
            observeTracking = ObserveTrackingUseCase(trackingRepository),
            startTracking = StartTrackingUseCase(trackingRepository),
            stopTracking = StopTrackingUseCase(trackingRepository),
            resetRoute = ResetRouteUseCase(routeRepository),
            getAddress = GetAddressUseCase(addressRepository),
        )
    }

    @Test
    fun savedRoute_isShownOnStart() = runTest {
        routeRepository.points.value = listOf(point(1), point(2))

        viewModel.uiState.test {
            assertThat(expectMostRecentItem().points.map { it.id }).containsExactly(1L, 2L)
        }
    }

    @Test
    fun startTracking_updatesState() = runTest {
        viewModel.uiState.test {
            viewModel.onStartTracking()

            assertThat(expectMostRecentItem().isTracking).isTrue()
        }
    }

    @Test
    fun startTracking_whenServiceFails_emitsStartFailed() = runTest {
        trackingRepository.canStart = false

        viewModel.effects.test {
            viewModel.onStartTracking()

            assertThat(awaitItem()).isEqualTo(TrackingUiEffect.StartFailed)
        }
    }

    @Test
    fun markerClick_showsGeocodedAddress() = runTest {
        routeRepository.points.value = listOf(point(1), point(2))
        addressRepository.address = "Moda, Kadıköy"

        viewModel.uiState.test {
            viewModel.onMarkerClick(2L)

            val selected = expectMostRecentItem().selectedPoint
            assertThat(selected?.order).isEqualTo(2)
            assertThat(selected?.address).isEqualTo(AddressState.Loaded("Moda, Kadıköy"))
        }
    }

    @Test
    fun markerClick_withoutAddress_showsUnavailable() = runTest {
        routeRepository.points.value = listOf(point(1))
        addressRepository.address = null

        viewModel.uiState.test {
            viewModel.onMarkerClick(1L)

            assertThat(
                expectMostRecentItem().selectedPoint?.address,
            ).isEqualTo(AddressState.Unavailable)
        }
    }

    @Test
    fun resetConfirm_clearsRouteAndClosesDialog() = runTest {
        routeRepository.points.value = listOf(point(1))

        viewModel.uiState.test {
            viewModel.onResetClick()
            assertThat(expectMostRecentItem().isResetDialogVisible).isTrue()

            viewModel.onResetConfirm()

            val state = expectMostRecentItem()
            assertThat(state.points).isEmpty()
            assertThat(state.isResetDialogVisible).isFalse()
        }
    }

    @Test
    fun screenStarted_resumesTrackingWhenFlagIsOn() = runTest {
        trackingRepository.isTracking.value = true

        viewModel.onScreenStarted(hasLocationPermission = true)

        assertThat(trackingRepository.startCalls).isEqualTo(1)
    }

    @Test
    fun screenStarted_withoutPermission_stopsTracking() = runTest {
        trackingRepository.isTracking.value = true

        viewModel.onScreenStarted(hasLocationPermission = false)

        assertThat(trackingRepository.isTracking.value).isFalse()
        assertThat(trackingRepository.startCalls).isEqualTo(0)
    }

    private fun point(id: Long) = RoutePoint(
        id = id,
        coordinate = Coordinate(41.0 + id / 1000.0, 29.0),
        accuracyMeters = 5f,
        recordedAtMillis = 0L,
        address = null,
    )
}

private class FakeRouteRepository : RouteRepository {
    val points = MutableStateFlow<List<RoutePoint>>(emptyList())

    override fun observeRoute(): Flow<List<RoutePoint>> = points

    override suspend fun lastPoint(): RoutePoint? = points.value.lastOrNull()

    override suspend fun addPoint(fix: LocationFix): RoutePoint = error("Not used")

    override suspend fun clear() {
        points.value = emptyList()
    }
}

private class FakeTrackingRepository : TrackingRepository {
    override val isTracking = MutableStateFlow(false)
    var canStart = true
    var startCalls = 0

    override suspend fun startTracking(): Boolean {
        startCalls++
        isTracking.value = canStart
        return canStart
    }

    override suspend fun stopTracking() {
        isTracking.value = false
    }
}

private class FakeAddressRepository : AddressRepository {
    var address: String? = null

    override suspend fun getAddress(point: RoutePoint): String? = address
}
