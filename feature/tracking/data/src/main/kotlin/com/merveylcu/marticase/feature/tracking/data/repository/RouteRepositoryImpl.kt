package com.merveylcu.marticase.feature.tracking.data.repository

import com.merveylcu.marticase.core.database.dao.RoutePointDao
import com.merveylcu.marticase.feature.tracking.data.mapper.toDomain
import com.merveylcu.marticase.feature.tracking.data.mapper.toEntity
import com.merveylcu.marticase.feature.tracking.domain.model.LocationFix
import com.merveylcu.marticase.feature.tracking.domain.model.RoutePoint
import com.merveylcu.marticase.feature.tracking.domain.repository.RouteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class RouteRepositoryImpl @Inject constructor(private val dao: RoutePointDao) :
    RouteRepository {
    override fun observeRoute(): Flow<List<RoutePoint>> =
        dao.observeAll().map { points -> points.map { it.toDomain() } }

    override suspend fun lastPoint(): RoutePoint? = dao.getLast()?.toDomain()

    override suspend fun addPoint(fix: LocationFix): RoutePoint {
        val entity = fix.toEntity()
        val id = dao.insert(entity)
        return entity.copy(id = id).toDomain()
    }

    override suspend fun clear() {
        dao.deleteAll()
    }
}
