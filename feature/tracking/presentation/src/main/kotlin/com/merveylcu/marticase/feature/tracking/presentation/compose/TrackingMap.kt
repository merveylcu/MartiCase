package com.merveylcu.marticase.feature.tracking.presentation.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.clustering.rememberClusterManager
import com.google.maps.android.compose.rememberCameraPositionState
import com.merveylcu.marticase.core.designsystem.component.MartiZoomControls
import com.merveylcu.marticase.core.designsystem.theme.MartiCaseTheme
import com.merveylcu.marticase.feature.tracking.domain.model.Coordinate
import com.merveylcu.marticase.feature.tracking.domain.model.RoutePoint
import com.merveylcu.marticase.feature.tracking.presentation.R
import com.merveylcu.marticase.feature.tracking.presentation.model.RoutePointItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch

private val DefaultTarget = LatLng(41.0082, 28.9784)
private const val DEFAULT_ZOOM = 11f
private const val FOLLOW_ZOOM = 16f
private const val ROUTE_WIDTH = 12f
private const val CLUSTER_ZOOM_STEP = 2f

@Composable
internal fun TrackingMap(
    points: ImmutableList<RoutePoint>,
    hasLocationPermission: Boolean,
    contentPadding: PaddingValues,
    onMarkerClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val cameraState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            points.lastOrNull()?.coordinate?.toLatLng() ?: DefaultTarget,
            if (points.isEmpty()) DEFAULT_ZOOM else FOLLOW_ZOOM,
        )
    }
    val scope = rememberCoroutineScope()
    val lastPoint = points.lastOrNull()
    LaunchedEffect(lastPoint?.id) {
        lastPoint?.let {
            cameraState.animate(
                CameraUpdateFactory.newLatLngZoom(it.coordinate.toLatLng(), FOLLOW_ZOOM),
            )
        }
    }

    Box(modifier = modifier) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraState,
            contentPadding = contentPadding,
            properties = MapProperties(isMyLocationEnabled = hasLocationPermission),
            uiSettings = MapUiSettings(zoomControlsEnabled = false, mapToolbarEnabled = false),
        ) {
            val route = remember(points) { points.map { it.coordinate.toLatLng() } }
            val items = remember(points) { points.map(::RoutePointItem) }
            if (route.size > 1) {
                Polyline(points = route, color = MartiCaseTheme.colors.primary, width = ROUTE_WIDTH)
            }
            RouteClustering(
                items = items,
                onClusterClick = { position ->
                    scope.launch {
                        cameraState.animate(
                            CameraUpdateFactory.newLatLngZoom(
                                position,
                                cameraState.position.zoom + CLUSTER_ZOOM_STEP,
                            ),
                        )
                    }
                },
                onItemClick = onMarkerClick,
            )
        }
        MartiZoomControls(
            zoomInDescription = stringResource(R.string.map_zoom_in),
            zoomOutDescription = stringResource(R.string.map_zoom_out),
            onZoomIn = { scope.launch { cameraState.animate(CameraUpdateFactory.zoomIn()) } },
            onZoomOut = { scope.launch { cameraState.animate(CameraUpdateFactory.zoomOut()) } },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = MartiCaseTheme.spacing.md),
        )
    }
}

private fun Coordinate.toLatLng() = LatLng(latitude, longitude)

@OptIn(MapsComposeExperimentalApi::class)
@Composable
private fun RouteClustering(
    items: List<RoutePointItem>,
    onClusterClick: (LatLng) -> Unit,
    onItemClick: (Long) -> Unit,
) {
    val context = LocalContext.current
    val clusterColor = MartiCaseTheme.colors.primary.toArgb()
    val currentOnClusterClick by rememberUpdatedState(onClusterClick)
    val currentOnItemClick by rememberUpdatedState(onItemClick)
    val clusterManager = rememberClusterManager<RoutePointItem>() ?: return

    MapEffect(clusterManager, clusterColor) { map ->
        clusterManager.renderer = RouteClusterRenderer(context, map, clusterManager, clusterColor)
        clusterManager.setOnClusterClickListener { cluster ->
            currentOnClusterClick(cluster.position)
            true
        }
        clusterManager.setOnClusterItemClickListener { item ->
            currentOnItemClick(item.id)
            true
        }
    }
    Clustering(items = items, clusterManager = clusterManager)
}
