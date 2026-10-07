package com.merveylcu.marticase.feature.tracking.data.service

import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class TrackingServiceState @Inject constructor() {
    private val running = AtomicBoolean(false)

    var isRunning: Boolean
        get() = running.get()
        set(value) = running.set(value)
}
