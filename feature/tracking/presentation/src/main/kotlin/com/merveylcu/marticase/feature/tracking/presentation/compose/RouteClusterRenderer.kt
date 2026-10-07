package com.merveylcu.marticase.feature.tracking.presentation.compose

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.merveylcu.marticase.feature.tracking.presentation.model.RoutePointItem

internal class RouteClusterRenderer(
    context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<RoutePointItem>,
    private val clusterColor: Int,
) : DefaultClusterRenderer<RoutePointItem>(context, map, clusterManager) {
    private val markerIcon: BitmapDescriptor by lazy {
        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
    }

    override fun onBeforeClusterItemRendered(item: RoutePointItem, markerOptions: MarkerOptions) {
        markerOptions.icon(markerIcon)
    }

    override fun onClusterItemUpdated(item: RoutePointItem, marker: Marker) {
        marker.setIcon(markerIcon)
    }

    override fun getColor(clusterSize: Int): Int = clusterColor
}
