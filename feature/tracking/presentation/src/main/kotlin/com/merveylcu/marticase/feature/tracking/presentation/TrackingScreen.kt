package com.merveylcu.marticase.feature.tracking.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.merveylcu.marticase.core.designsystem.component.MartiSnackbarHost
import com.merveylcu.marticase.core.designsystem.theme.MartiCaseTheme
import com.merveylcu.marticase.core.permission.hasFineLocationPermission
import com.merveylcu.marticase.core.permission.rememberLocationTrackingPermissionFlow
import com.merveylcu.marticase.feature.tracking.presentation.compose.PointDetailSheet
import com.merveylcu.marticase.feature.tracking.presentation.compose.ResetRouteDialog
import com.merveylcu.marticase.feature.tracking.presentation.compose.TrackingControls
import com.merveylcu.marticase.feature.tracking.presentation.compose.TrackingMap
import com.merveylcu.marticase.feature.tracking.presentation.compose.TrackingStatusChip
import com.merveylcu.marticase.feature.tracking.presentation.model.UserMessage

@Composable
fun TrackingScreen(modifier: Modifier = Modifier, viewModel: TrackingViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    var hasLocationPermission by remember { mutableStateOf(context.hasFineLocationPermission()) }

    val startTrackingFlow = rememberLocationTrackingPermissionFlow(
        onReady = {
            hasLocationPermission = true
            viewModel.onStartTracking()
        },
        onBlock = viewModel::onStartBlocked,
    )

    LifecycleStartEffect(viewModel) {
        hasLocationPermission = context.hasFineLocationPermission()
        viewModel.onScreenStarted(hasLocationPermission)
        onStopOrDispose { }
    }

    UserMessageSnackbar(
        message = state.userMessage,
        snackbarHostState = snackbarHostState,
        onShow = viewModel::onUserMessageShown,
    )

    TrackingContent(
        state = state,
        hasLocationPermission = hasLocationPermission,
        snackbarHostState = snackbarHostState,
        onStartClick = startTrackingFlow,
        onStopClick = viewModel::onStopTracking,
        onResetClick = viewModel::onResetClick,
        onResetConfirm = viewModel::onResetConfirm,
        onResetDismiss = viewModel::onResetDismiss,
        onMarkerClick = viewModel::onMarkerClick,
        onPointDismiss = viewModel::onPointDismiss,
        modifier = modifier,
    )
}

@Composable
private fun UserMessageSnackbar(
    message: UserMessage?,
    snackbarHostState: SnackbarHostState,
    onShow: () -> Unit,
) {
    val text = message?.let { stringResource(it.textRes) }
    val currentOnShow by rememberUpdatedState(onShow)
    LaunchedEffect(message) {
        if (text != null) {
            snackbarHostState.showSnackbar(text)
            currentOnShow()
        }
    }
}

private val UserMessage.textRes: Int
    get() = when (this) {
        UserMessage.StartFailed -> R.string.error_start_failed
        UserMessage.PreciseLocationRequired -> R.string.error_precise_location_required
        UserMessage.LocationDisabled -> R.string.error_location_disabled
    }

@Composable
internal fun TrackingContent(
    state: TrackingUiState,
    hasLocationPermission: Boolean,
    snackbarHostState: SnackbarHostState,
    onStartClick: () -> Unit,
    onStopClick: () -> Unit,
    onResetClick: () -> Unit,
    onResetConfirm: () -> Unit,
    onResetDismiss: () -> Unit,
    onMarkerClick: (Long) -> Unit,
    onPointDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = MartiCaseTheme.spacing
    val controlHeight = MartiCaseTheme.dimens.controlHeight
    val systemBars = WindowInsets.systemBars.asPaddingValues()

    Box(modifier = modifier.fillMaxSize()) {
        TrackingMap(
            points = state.points,
            hasLocationPermission = hasLocationPermission,
            contentPadding = PaddingValues(
                top = systemBars.calculateTopPadding() + controlHeight,
                bottom = systemBars.calculateBottomPadding() + controlHeight + spacing.lg,
            ),
            onMarkerClick = onMarkerClick,
            modifier = Modifier.fillMaxSize(),
        )

        TrackingStatusChip(
            isTracking = state.isTracking,
            markerCount = state.points.size,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = spacing.sm),
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(spacing.md),
        ) {
            MartiSnackbarHost(hostState = snackbarHostState)
            TrackingControls(
                isTracking = state.isTracking,
                canReset = state.points.isNotEmpty(),
                onStartClick = onStartClick,
                onStopClick = onStopClick,
                onResetClick = onResetClick,
            )
        }
    }

    state.selectedPoint?.let { selected ->
        PointDetailSheet(selected = selected, onDismiss = onPointDismiss)
    }
    if (state.isResetDialogVisible) {
        ResetRouteDialog(onConfirm = onResetConfirm, onDismiss = onResetDismiss)
    }
}
