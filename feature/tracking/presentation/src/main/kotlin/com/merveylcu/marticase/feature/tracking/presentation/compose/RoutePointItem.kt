package com.merveylcu.marticase.feature.tracking.presentation.compose

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.merveylcu.marticase.feature.tracking.domain.model.RoutePoint

internal class RoutePointItem(point: RoutePoint) : ClusterItem {
    val id: Long = point.id
    override val position: LatLng = LatLng(point.coordinate.latitude, point.coordinate.longitude)
    override val title: String? = null
    override val snippet: String? = null
    override val zIndex: Float? = null
}
