package com.merveylcu.marticase.feature.tracking.domain.usecase

import com.merveylcu.marticase.feature.tracking.domain.repository.RouteRepository
import javax.inject.Inject

public class ResetRouteUseCase @Inject constructor(private val repository: RouteRepository) {
    public suspend operator fun invoke() {
        repository.clear()
    }
}
