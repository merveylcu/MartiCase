package com.merveylcu.marticase.feature.tracking.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.merveylcu.marticase.core.database.dao.RoutePointDao
import com.merveylcu.marticase.core.database.entity.RoutePointEntity
import com.merveylcu.marticase.feature.tracking.domain.model.Coordinate
import com.merveylcu.marticase.feature.tracking.domain.model.LocationFix
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RouteRepositoryImplTest {
    private val dao = mockk<RoutePointDao>(relaxUnitFun = true)
    private val repository = RouteRepositoryImpl(dao)

    @Test
    fun observeRoute_mapsEntitiesToDomain() = runTest {
        every { dao.observeAll() } returns flowOf(listOf(entity(id = 1, address = "Moda")))

        repository.observeRoute().test {
            val point = awaitItem().single()
            assertThat(point.id).isEqualTo(1)
            assertThat(point.coordinate).isEqualTo(Coordinate(41.0, 29.0))
            assertThat(point.address).isEqualTo("Moda")
            awaitComplete()
        }
    }

    @Test
    fun addPoint_returnsPointWithGeneratedId() = runTest {
        coEvery { dao.insert(any()) } returns 7L

        val point = repository.addPoint(
            LocationFix(Coordinate(41.0, 29.0), accuracyMeters = 4f, timeMillis = 100L),
        )

        assertThat(point.id).isEqualTo(7L)
        assertThat(point.recordedAtMillis).isEqualTo(100L)
        assertThat(point.address).isNull()
    }

    @Test
    fun clear_deletesAllPoints() = runTest {
        repository.clear()

        coVerify { dao.deleteAll() }
    }

    private fun entity(id: Long, address: String?) = RoutePointEntity(
        id = id,
        latitude = 41.0,
        longitude = 29.0,
        accuracyMeters = 5f,
        recordedAtMillis = 0L,
        address = address,
    )
}
