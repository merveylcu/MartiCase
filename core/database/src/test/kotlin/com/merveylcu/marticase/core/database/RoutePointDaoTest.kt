package com.merveylcu.marticase.core.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.merveylcu.marticase.core.database.dao.RoutePointDao
import com.merveylcu.marticase.core.database.entity.RoutePointEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoutePointDaoTest {
    private lateinit var database: MartiCaseDatabase
    private lateinit var dao: RoutePointDao

    @Before
    fun setUp() {
        database = Room
            .inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                MartiCaseDatabase::class.java,
            ).build()
        dao = database.routePointDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun getLast_returnsMostRecentlyInsertedPoint() = runTest {
        dao.insert(point(latitude = 41.0))
        dao.insert(point(latitude = 41.1))

        assertThat(dao.getLast()?.latitude).isEqualTo(41.1)
    }

    @Test
    fun updateAddress_storesAddressForPoint() = runTest {
        val id = dao.insert(point(latitude = 41.0))

        dao.updateAddress(id, "Kadıköy, İstanbul")

        assertThat(dao.getById(id)?.address).isEqualTo("Kadıköy, İstanbul")
    }

    @Test
    fun observeAll_emitsPointsInInsertOrder() = runTest {
        dao.insert(point(latitude = 41.0))
        dao.insert(point(latitude = 41.1))
        dao.insert(point(latitude = 41.2))

        assertThat(dao.observeAll().first().map { it.latitude })
            .containsExactly(41.0, 41.1, 41.2)
            .inOrder()
    }

    @Test
    fun getLast_onEmptyRoute_isNull() = runTest {
        assertThat(dao.getLast()).isNull()
    }

    @Test
    fun deleteAll_clearsRoute() = runTest {
        dao.insert(point(latitude = 41.0))
        dao.insert(point(latitude = 41.1))

        dao.deleteAll()

        assertThat(dao.observeAll().first()).isEmpty()
        assertThat(dao.getLast()).isNull()
    }

    private fun point(latitude: Double) = RoutePointEntity(
        latitude = latitude,
        longitude = 29.0,
        accuracyMeters = 5f,
        recordedAtMillis = 0L,
    )
}
