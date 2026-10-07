package com.merveylcu.marticase.feature.tracking.data.service

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.database.sqlite.SQLiteException
import android.os.Build
import android.os.IBinder
import androidx.core.app.ServiceCompat
import com.merveylcu.marticase.core.common.dispatcher.ApplicationScope
import com.merveylcu.marticase.core.common.dispatcher.DefaultDispatcher
import com.merveylcu.marticase.feature.tracking.data.location.LocationClient
import com.merveylcu.marticase.feature.tracking.domain.repository.TrackingRepository
import com.merveylcu.marticase.feature.tracking.domain.usecase.RecordLocationUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
internal class TrackingService : Service() {
    @Inject
    lateinit var locationClient: LocationClient

    @Inject
    lateinit var recordLocation: RecordLocationUseCase

    @Inject
    lateinit var trackingRepository: TrackingRepository

    @Inject
    @DefaultDispatcher
    lateinit var dispatcher: CoroutineDispatcher

    @Inject
    @ApplicationScope
    lateinit var applicationScope: CoroutineScope

    private val scope by lazy {
        CoroutineScope(
            SupervisorJob() + dispatcher + CoroutineExceptionHandler { _, _ ->
                stopTracking()
            },
        )
    }
    private var updatesJob: Job? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        if (intent?.action == ACTION_STOP) {
            stopTracking()
            return START_NOT_STICKY
        }
        if (!startInForeground()) {
            stopSelf(startId)
            return START_NOT_STICKY
        }
        if (!locationClient.hasPermission()) {
            stopTracking()
            return START_NOT_STICKY
        }

        if (intent == null) {
            scope.launch {
                if (trackingRepository.isTracking.first()) {
                    startLocationUpdates()
                } else {
                    stopSelf(
                        startId,
                    )
                }
            }
        } else {
            startLocationUpdates()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    private fun startInForeground(): Boolean {
        val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
        } else {
            0
        }
        return try {
            ServiceCompat.startForeground(
                this,
                TRACKING_NOTIFICATION_ID,
                createTrackingNotification(),
                type,
            )
            true
        } catch (_: IllegalStateException) {
            false
        } catch (_: SecurityException) {
            false
        }
    }

    private fun startLocationUpdates() {
        if (updatesJob?.isActive == true) return
        updatesJob = scope.launch {
            locationClient
                .locationUpdates()
                .catch { stopTracking() }
                .collect { fix ->
                    try {
                        recordLocation(fix)
                    } catch (_: SQLiteException) {
                    }
                }
        }
    }

    private fun stopTracking() {
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
        applicationScope.launch { trackingRepository.stopTracking() }
        stopSelf()
    }

    companion object {
        const val ACTION_STOP: String = "com.merveylcu.marticase.action.STOP_TRACKING"
    }
}
