package com.merveylcu.marticase.feature.tracking.data.service

import android.Manifest
import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.merveylcu.marticase.feature.tracking.data.location.LocationClient
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf

@RunWith(AndroidJUnit4::class)
class TrackingServiceControllerTest {
    private val application = ApplicationProvider.getApplicationContext<Application>()
    private val controller = TrackingServiceController(application, LocationClient(application))

    @Test
    fun start_withoutLocationPermission_returnsFalseAndStartsNothing() {
        assertThat(controller.start()).isFalse()
        assertThat(shadowOf(application).nextStartedService).isNull()
    }

    @Test
    fun start_withLocationPermission_startsTrackingService() {
        shadowOf(application).grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION)

        assertThat(controller.start()).isTrue()
        assertThat(shadowOf(application).nextStartedService.component?.className)
            .isEqualTo(TrackingService::class.java.name)
    }

    @Test
    fun stop_stopsTrackingService() {
        controller.stop()

        assertThat(shadowOf(application).nextStoppedService.component?.className)
            .isEqualTo(TrackingService::class.java.name)
    }
}
