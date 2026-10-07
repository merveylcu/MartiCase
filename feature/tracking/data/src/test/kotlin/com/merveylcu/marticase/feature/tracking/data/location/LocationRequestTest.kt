package com.merveylcu.marticase.feature.tracking.data.location

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocationRequestTest {
    @Test
    fun liveRequest_deliversEachFixRightAway() {
        val request = locationRequest(batched = false)

        assertThat(request.maxUpdateDelayMillis).isEqualTo(request.intervalMillis)
    }

    @Test
    fun batchedRequest_allowsDelayedDelivery() {
        val request = locationRequest(batched = true)

        assertThat(request.maxUpdateDelayMillis).isEqualTo(60_000L)
    }

    @Test
    fun batchedFixes_arentOlderThanTheStaleLimit() {
        val request = locationRequest(batched = true)

        val oldestBatchedFixNanos =
            (request.maxUpdateDelayMillis + request.intervalMillis) * 1_000_000
        assertThat(oldestBatchedFixNanos).isLessThan(MAX_FIX_AGE_NANOS)
    }
}
