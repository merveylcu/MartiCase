package com.merveylcu.marticase.feature.tracking.presentation.permission

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

private typealias Step = () -> Unit

/**
 * Runs the steps needed before tracking can start, in the order the service needs them:
 * precise location -> notifications (13+) -> battery exemption -> device location on.
 * Returns a function that starts the chain.
 */
@Composable
internal fun rememberStartTrackingFlow(
    onReady: () -> Unit,
    onBlock: (StartBlocker) -> Unit,
): Step {
    val currentOnReady by rememberUpdatedState(onReady)
    val currentOnBlock by rememberUpdatedState(onBlock)

    val locationSettings = rememberLocationSettingsStep(
        onEnable = { currentOnReady() },
        onDisable = { currentOnBlock(StartBlocker.LocationDisabled) },
    )
    val battery = rememberBatteryStep(next = locationSettings)
    val notifications = rememberNotificationStep(next = battery)
    return rememberLocationPermissionStep(
        next = notifications,
        onDeny = { currentOnBlock(StartBlocker.PreciseLocationDenied) },
    )
}

@Composable
private fun rememberLocationPermissionStep(next: Step, onDeny: () -> Unit): Step {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { result ->
        // Coarse-only fixes can't pass the 50 m accuracy filter (ADR 0005).
        if (result[Manifest.permission.ACCESS_FINE_LOCATION] == true) next() else onDeny()
    }
    return {
        if (context.hasFineLocationPermission()) {
            next()
        } else {
            launcher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ),
            )
        }
    }
}

// Tracking still works without this permission; the notification is just hidden.
@Composable
private fun rememberNotificationStep(next: Step): Step {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { next() }
    return {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            next()
        }
    }
}

// Asked once per screen so a "no" isn't repeated on every start.
@Composable
private fun rememberBatteryStep(next: Step): Step {
    val context = LocalContext.current
    var asked by rememberSaveable { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { next() }
    return {
        if (!asked && !context.isIgnoringBatteryOptimizations()) {
            asked = true
            launcher.launch(context.batteryOptimizationIntent())
        } else {
            next()
        }
    }
}

@Composable
private fun rememberLocationSettingsStep(onEnable: () -> Unit, onDisable: () -> Unit): Step {
    val context = LocalContext.current
    return {
        if (context.isLocationEnabled()) {
            onEnable()
        } else {
            onDisable()
            context.startActivity(locationSettingsIntent())
        }
    }
}
