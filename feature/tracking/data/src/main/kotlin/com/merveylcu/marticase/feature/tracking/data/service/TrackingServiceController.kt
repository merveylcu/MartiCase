package com.merveylcu.marticase.feature.tracking.data.service

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.merveylcu.marticase.feature.tracking.data.location.LocationClient
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class TrackingServiceController @Inject constructor(
    @ApplicationContext private val context: Context,
    private val locationClient: LocationClient,
) {
    fun start(): Boolean {
        if (!locationClient.hasPermission()) return false
        return try {
            ContextCompat.startForegroundService(context, serviceIntent())
            true
        } catch (_: IllegalStateException) {
            false
        }
    }

    fun stop() {
        context.stopService(serviceIntent())
    }

    private fun serviceIntent() = Intent(context, TrackingService::class.java)
}
