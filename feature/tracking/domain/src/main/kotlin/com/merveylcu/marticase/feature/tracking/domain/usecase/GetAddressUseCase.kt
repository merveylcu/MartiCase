package com.merveylcu.marticase.feature.tracking.domain.usecase

import com.merveylcu.marticase.feature.tracking.domain.model.RoutePoint
import com.merveylcu.marticase.feature.tracking.domain.repository.AddressRepository
import javax.inject.Inject

public class GetAddressUseCase
@Inject
constructor(private val repository: AddressRepository) {
    public suspend operator fun invoke(point: RoutePoint): String? =
        point.address ?: repository.getAddress(point)
}
