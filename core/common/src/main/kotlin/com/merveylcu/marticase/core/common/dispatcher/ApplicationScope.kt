package com.merveylcu.marticase.core.common.dispatcher

import javax.inject.Qualifier

/** App-wide [kotlinx.coroutines.CoroutineScope] for work that must outlive a screen. */
@Qualifier
@Retention(AnnotationRetention.BINARY)
public annotation class ApplicationScope
