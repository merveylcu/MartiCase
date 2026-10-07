package com.merveylcu.marticase.feature.tracking.presentation.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.merveylcu.marticase.core.designsystem.theme.MartiCaseTheme
import com.merveylcu.marticase.feature.tracking.domain.model.Coordinate
import com.merveylcu.marticase.feature.tracking.domain.model.RoutePoint
import kotlinx.collections.immutable.ImmutableList

private val DefaultTarget = LatLng(41.0082, 28.9784)
private const val DEFAULT_ZOOM = 11f
private const val FOLLOW_ZOOM = 16f
private const val ROUTE_WIDTH = 12f

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
    val lastPoint = points.lastOrNull()
    LaunchedEffect(lastPoint?.id) {
        lastPoint?.let {
            cameraState.animate(
                CameraUpdateFactory.newLatLngZoom(it.coordinate.toLatLng(), FOLLOW_ZOOM),
            )
        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraState,
        contentPadding = contentPadding,
        properties = MapProperties(isMyLocationEnabled = hasLocationPermission),
        uiSettings = MapUiSettings(zoomControlsEnabled = false, mapToolbarEnabled = false),
    ) {
        val markerIcon =
            remember { BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN) }
        val route = remember(points) { points.map { it.coordinate.toLatLng() } }
        if (route.size > 1) {
            Polyline(points = route, color = MartiCaseTheme.colors.primary, width = ROUTE_WIDTH)
        }
        points.forEach { point ->
            key(point.id) {
                Marker(
                    state = rememberUpdatedMarkerState(point.coordinate.toLatLng()),
                    icon = markerIcon,
                    onClick = {
                        onMarkerClick(point.id)
                        true
                    },
                )
            }
        }
    }
}

private fun Coordinate.toLatLng() = LatLng(latitude, longitude)
