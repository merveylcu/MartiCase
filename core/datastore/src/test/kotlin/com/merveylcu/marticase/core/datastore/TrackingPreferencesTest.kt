package com.merveylcu.marticase.core.datastore

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class TrackingPreferencesTest {
    @get:Rule
    val tempFolder = TemporaryFolder()

    private fun TestScope.preferences() = TrackingPreferences(
        PreferenceDataStoreFactory.create(
            scope = backgroundScope,
            produceFile = { File(tempFolder.root, "tracking.preferences_pb") },
        ),
    )

    @Test
    fun isTracking_defaultsToFalse() = runTest {
        preferences().isTracking.test {
            assertThat(awaitItem()).isFalse()
        }
    }

    @Test
    fun setTracking_isReadBack() = runTest {
        val preferences = preferences()

        preferences.isTracking.test {
            assertThat(awaitItem()).isFalse()

            preferences.setTracking(true)
            assertThat(awaitItem()).isTrue()

            preferences.setTracking(false)
            assertThat(awaitItem()).isFalse()
        }
    }
}
