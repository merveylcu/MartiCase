package com.merveylcu.marticase.feature.tracking.data.repository

import com.google.common.truth.Truth.assertThat
import com.merveylcu.marticase.core.database.dao.RoutePointDao
import com.merveylcu.marticase.core.database.entity.RoutePointEntity
import com.merveylcu.marticase.feature.tracking.data.geocoder.AddressGeocoder
import com.merveylcu.marticase.feature.tracking.domain.model.Coordinate
import com.merveylcu.marticase.feature.tracking.domain.model.RoutePoint
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AddressRepositoryImplTest {
    private val geocoder = mockk<AddressGeocoder>()
    private val dao = mockk<RoutePointDao>(relaxUnitFun = true)
    private val repository = AddressRepositoryImpl(geocoder, dao)
    private val point = RoutePoint(1L, Coordinate(41.0, 29.0), 5f, 0L, address = null)

    @Test
    fun cachedAddressInDatabase_skipsGeocoder() = runTest {
        coEvery { dao.getById(1L) } returns entity(address = "Moda")

        assertThat(repository.getAddress(point)).isEqualTo("Moda")
        coVerify(exactly = 0) { geocoder.reverseGeocode(any()) }
    }

    @Test
    fun geocodedAddress_isCached() = runTest {
        coEvery { dao.getById(1L) } returns entity(address = null)
        coEvery { geocoder.reverseGeocode(point.coordinate) } returns "Moda"

        assertThat(repository.getAddress(point)).isEqualTo("Moda")
        coVerify { dao.updateAddress(1L, "Moda") }
    }

    @Test
    fun noGeocoderResult_returnsNullAndCachesNothing() = runTest {
        coEvery { dao.getById(1L) } returns entity(address = null)
        coEvery { geocoder.reverseGeocode(any()) } returns null

        assertThat(repository.getAddress(point)).isNull()
        coVerify(exactly = 0) { dao.updateAddress(any(), any()) }
    }

    private fun entity(address: String?) = RoutePointEntity(
        id = 1L,
        latitude = 41.0,
        longitude = 29.0,
        accuracyMeters = 5f,
        recordedAtMillis = 0L,
        address = address,
    )
}
