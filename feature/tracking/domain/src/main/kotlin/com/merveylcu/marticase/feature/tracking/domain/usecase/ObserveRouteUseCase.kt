package com.merveylcu.marticase.feature.tracking.domain.usecase

import com.merveylcu.marticase.feature.tracking.domain.model.RoutePoint
import com.merveylcu.marticase.feature.tracking.domain.repository.RouteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

public class ObserveRouteUseCase
@Inject
constructor(private val repository: RouteRepository) {
    public operator fun invoke(): Flow<List<RoutePoint>> = repository.observeRoute()
}
