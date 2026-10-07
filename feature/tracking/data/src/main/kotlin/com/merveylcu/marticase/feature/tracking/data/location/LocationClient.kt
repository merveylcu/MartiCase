package com.merveylcu.marticase.feature.tracking.data.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.os.SystemClock
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.merveylcu.marticase.feature.tracking.domain.model.LocationFix
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

private const val INTERVAL_MILLIS = 10_000L
private const val MIN_INTERVAL_MILLIS = 5_000L

private const val MIN_DISTANCE_METERS = 20f
private const val BACKGROUND_MAX_UPDATE_DELAY_MILLIS = 60_000L

internal class LocationClient @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val client = LocationServices.getFusedLocationProviderClient(context)

    fun hasPermission(): Boolean = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION,
    ) == PackageManager.PERMISSION_GRANTED

    fun locationUpdates(batched: Boolean): Flow<LocationFix> = callbackFlow {
        val request = locationRequest(batched)

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val now = SystemClock.elapsedRealtimeNanos()
                result.locations
                    .mapNotNull { it.toLocationFixOrNull(now) }
                    .forEach { trySend(it) }
            }
        }

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            close(SecurityException("Location permission is not granted"))
            return@callbackFlow
        }
        client
            .requestLocationUpdates(request, callback, Looper.getMainLooper())
            .addOnFailureListener { close(it) }

        awaitClose { client.removeLocationUpdates(callback) }
    }
}

internal fun locationRequest(batched: Boolean): LocationRequest = LocationRequest
    .Builder(Priority.PRIORITY_HIGH_ACCURACY, INTERVAL_MILLIS)
    .setMinUpdateIntervalMillis(MIN_INTERVAL_MILLIS)
    .setMinUpdateDistanceMeters(MIN_DISTANCE_METERS)
    .setMaxUpdateDelayMillis(if (batched) BACKGROUND_MAX_UPDATE_DELAY_MILLIS else 0L)
    .build()
