package com.merveylcu.marticase.core.permission

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.PowerManager
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import androidx.core.net.toUri

fun Context.hasFineLocationPermission(): Boolean =
    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
        PackageManager.PERMISSION_GRANTED

fun Context.isLocationEnabled(): Boolean {
    val manager = getSystemService(LocationManager::class.java) ?: return false
    return LocationManagerCompat.isLocationEnabled(manager)
}

fun Context.isIgnoringBatteryOptimizations(): Boolean =
    getSystemService(PowerManager::class.java)?.isIgnoringBatteryOptimizations(packageName) ?: true

@SuppressLint("BatteryLife")
fun Context.batteryOptimizationIntent(): Intent =
    Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS, "package:$packageName".toUri())

fun locationSettingsIntent(): Intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
