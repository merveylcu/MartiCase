package com.merveylcu.marticase.feature.tracking.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.merveylcu.marticase.feature.tracking.domain.usecase.GetAddressUseCase
import com.merveylcu.marticase.feature.tracking.domain.usecase.ObserveRouteUseCase
import com.merveylcu.marticase.feature.tracking.domain.usecase.ObserveTrackingUseCase
import com.merveylcu.marticase.feature.tracking.domain.usecase.ResetRouteUseCase
import com.merveylcu.marticase.feature.tracking.domain.usecase.StartTrackingUseCase
import com.merveylcu.marticase.feature.tracking.domain.usecase.StopTrackingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val STOP_TIMEOUT_MILLIS = 5_000L

@HiltViewModel
class TrackingViewModel @Inject constructor(
    private val observeRoute: ObserveRouteUseCase,
    private val observeTracking: ObserveTrackingUseCase,
    private val startTracking: StartTrackingUseCase,
    private val stopTracking: StopTrackingUseCase,
    private val resetRoute: ResetRouteUseCase,
    private val getAddress: GetAddressUseCase,
) : ViewModel() {

    private data class Selection(val pointId: Long, val address: AddressState)

    private val selection = MutableStateFlow<Selection?>(null)
    private val isResetDialogVisible = MutableStateFlow(false)
    private var addressJob: Job? = null

    val uiState: StateFlow<TrackingUiState> = combine(
        observeRoute(),
        observeTracking(),
        selection,
        isResetDialogVisible,
    ) { points, isTracking, selection, isResetDialogVisible ->
        val selectedIndex = selection?.let { s -> points.indexOfFirst { it.id == s.pointId } } ?: -1
        TrackingUiState(
            points = points.toImmutableList(),
            isTracking = isTracking,
            selectedPoint = selection?.takeIf { selectedIndex >= 0 }?.let {
                SelectedPoint(
                    points[selectedIndex],
                    order = selectedIndex + 1,
                    address = it.address,
                )
            },
            isResetDialogVisible = isResetDialogVisible,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        TrackingUiState(),
    )

    private val effectChannel = Channel<TrackingUiEffect>(Channel.BUFFERED)
    val effects: Flow<TrackingUiEffect> = effectChannel.receiveAsFlow()

    fun onStartTracking() {
        viewModelScope.launch {
            if (!startTracking()) effectChannel.send(TrackingUiEffect.StartFailed)
        }
    }

    fun onStopTracking() {
        viewModelScope.launch { stopTracking() }
    }

    fun onScreenStarted(hasLocationPermission: Boolean) {
        viewModelScope.launch {
            if (!observeTracking().first()) return@launch
            if (hasLocationPermission) startTracking() else stopTracking()
        }
    }

    fun onMarkerClick(pointId: Long) {
        selection.value = Selection(pointId, AddressState.Loading)
        addressJob?.cancel()
        addressJob = viewModelScope.launch {
            val point = observeRoute().first().find { it.id == pointId } ?: return@launch
            val address = getAddress(point)
                ?.let { AddressState.Loaded(it) }
                ?: AddressState.Unavailable
            selection.update { current ->
                current?.takeIf { it.pointId == pointId }?.copy(address = address)
            }
        }
    }

    fun onPointDismiss() {
        addressJob?.cancel()
        selection.value = null
    }

    fun onResetClick() {
        isResetDialogVisible.value = true
    }

    fun onResetDismiss() {
        isResetDialogVisible.value = false
    }

    fun onResetConfirm() {
        isResetDialogVisible.value = false
        onPointDismiss()
        viewModelScope.launch { resetRoute() }
    }
}
