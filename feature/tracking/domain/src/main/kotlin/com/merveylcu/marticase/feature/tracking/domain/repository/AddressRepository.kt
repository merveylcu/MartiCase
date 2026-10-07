package com.merveylcu.marticase.feature.tracking.domain.repository

import com.merveylcu.marticase.feature.tracking.domain.model.RoutePoint

public interface AddressRepository {
    /** Address for [point]: cached if known, otherwise geocoded and cached. Null if unavailable. */
    public suspend fun getAddress(point: RoutePoint): String?
}
