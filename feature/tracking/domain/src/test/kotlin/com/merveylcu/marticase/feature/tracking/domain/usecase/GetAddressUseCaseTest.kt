package com.merveylcu.marticase.feature.tracking.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.merveylcu.marticase.feature.tracking.domain.model.Coordinate
import com.merveylcu.marticase.feature.tracking.domain.model.RoutePoint
import com.merveylcu.marticase.feature.tracking.domain.repository.AddressRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetAddressUseCaseTest {
    private val repository = mockk<AddressRepository>()
    private val getAddress = GetAddressUseCase(repository)

    @Test
    fun cachedAddress_isReturnedWithoutGeocoding() = runTest {
        val address = getAddress(point(address = "Moda, Kadıköy"))

        assertThat(address).isEqualTo("Moda, Kadıköy")
        coVerify(exactly = 0) { repository.getAddress(any()) }
    }

    @Test
    fun missingAddress_isFetchedFromRepository() = runTest {
        val point = point(address = null)
        coEvery { repository.getAddress(point) } returns "Moda, Kadıköy"

        assertThat(getAddress(point)).isEqualTo("Moda, Kadıköy")
    }

    private fun point(address: String?) = RoutePoint(
        id = 1L,
        coordinate = Coordinate(40.98, 29.02),
        accuracyMeters = 5f,
        recordedAtMillis = 0L,
        address = address,
    )
}
