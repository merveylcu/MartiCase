package com.merveylcu.marticase.feature.tracking.data.service

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.merveylcu.marticase.feature.tracking.data.R
import com.merveylcu.marticase.feature.tracking.domain.usecase.RecordLocationUseCase

internal const val TRACKING_NOTIFICATION_ID = 1001
private const val CHANNEL_ID = "route_tracking"
private const val OPEN_APP_REQUEST_CODE = 1
private const val STOP_REQUEST_CODE = 2

internal fun Context.createTrackingNotification(): Notification {
    NotificationManagerCompat.from(this).createNotificationChannel(
        NotificationChannelCompat
            .Builder(CHANNEL_ID, NotificationManagerCompat.IMPORTANCE_LOW)
            .setName(getString(R.string.tracking_notification_channel))
            .build(),
    )

    val openApp = packageManager.getLaunchIntentForPackage(packageName)?.let { intent ->
        PendingIntent.getActivity(this, OPEN_APP_REQUEST_CODE, intent, PendingIntent.FLAG_IMMUTABLE)
    }
    val stop = PendingIntent.getService(
        this,
        STOP_REQUEST_CODE,
        Intent(this, TrackingService::class.java).setAction(TrackingService.ACTION_STOP),
        PendingIntent.FLAG_IMMUTABLE,
    )

    return NotificationCompat
        .Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_tracking_notification)
        .setContentTitle(getString(R.string.tracking_notification_title))
        .setContentText(
            getString(
                R.string.tracking_notification_text,
                RecordLocationUseCase.MARKER_DISTANCE_METERS.toInt(),
            ),
        )
        .setContentIntent(openApp)
        .addAction(0, getString(R.string.tracking_notification_stop), stop)
        .setOngoing(true)
        .setOnlyAlertOnce(true)
        .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
        .setCategory(NotificationCompat.CATEGORY_SERVICE)
        .build()
}
