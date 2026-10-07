package com.merveylcu.marticase.feature.tracking.domain.repository

import com.merveylcu.marticase.feature.tracking.domain.model.RoutePoint

public interface AddressRepository {
    public suspend fun getAddress(point: RoutePoint): String?
}
