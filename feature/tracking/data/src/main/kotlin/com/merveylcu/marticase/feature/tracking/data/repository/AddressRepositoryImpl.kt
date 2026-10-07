package com.merveylcu.marticase.feature.tracking.data.repository

import com.merveylcu.marticase.core.database.dao.RoutePointDao
import com.merveylcu.marticase.feature.tracking.data.geocoder.AddressGeocoder
import com.merveylcu.marticase.feature.tracking.domain.model.RoutePoint
import com.merveylcu.marticase.feature.tracking.domain.repository.AddressRepository
import javax.inject.Inject

internal class AddressRepositoryImpl @Inject constructor(
    private val geocoder: AddressGeocoder,
    private val dao: RoutePointDao,
) : AddressRepository {
    override suspend fun getAddress(point: RoutePoint): String? {
        dao.getById(point.id)?.address?.let { return it }
        val address = geocoder.reverseGeocode(point.coordinate) ?: return null
        dao.updateAddress(point.id, address)
        return address
    }
}
