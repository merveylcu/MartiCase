package com.merveylcu.marticase.feature.tracking.data.geocoder

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import com.merveylcu.marticase.core.common.dispatcher.IoDispatcher
import com.merveylcu.marticase.feature.tracking.domain.model.Coordinate
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.resume

/** Reverse geocodes with the platform [Geocoder]. Returns null when no address is available. */
internal class AddressGeocoder @Inject constructor(
    @ApplicationContext context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {
    private val geocoder = Geocoder(context)

    suspend fun reverseGeocode(coordinate: Coordinate): String? {
        if (!Geocoder.isPresent()) return null
        val address = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocodeAsync(coordinate)
        } else {
            geocodeBlocking(coordinate)
        }
        return address?.getAddressLine(0)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private suspend fun geocodeAsync(coordinate: Coordinate): Address? =
        suspendCancellableCoroutine { continuation ->
            geocoder.getFromLocation(
                coordinate.latitude,
                coordinate.longitude,
                1,
                object : Geocoder.GeocodeListener {
                    override fun onGeocode(addresses: MutableList<Address>) {
                        continuation.resume(addresses.firstOrNull())
                    }

                    override fun onError(errorMessage: String?) {
                        continuation.resume(null)
                    }
                },
            )
        }

    // The blocking overload is the only option below API 33, so it runs on the IO dispatcher.
    @Suppress("DEPRECATION")
    private suspend fun geocodeBlocking(coordinate: Coordinate): Address? =
        withContext(ioDispatcher) {
            try {
                geocoder
                    .getFromLocation(coordinate.latitude, coordinate.longitude, 1)
                    ?.firstOrNull()
            } catch (_: IOException) {
                null
            }
        }
}
