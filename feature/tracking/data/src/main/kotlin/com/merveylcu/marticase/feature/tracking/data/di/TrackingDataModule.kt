package com.merveylcu.marticase.feature.tracking.data.di

import com.merveylcu.marticase.feature.tracking.data.repository.AddressRepositoryImpl
import com.merveylcu.marticase.feature.tracking.data.repository.RouteRepositoryImpl
import com.merveylcu.marticase.feature.tracking.data.repository.TrackingRepositoryImpl
import com.merveylcu.marticase.feature.tracking.domain.repository.AddressRepository
import com.merveylcu.marticase.feature.tracking.domain.repository.RouteRepository
import com.merveylcu.marticase.feature.tracking.domain.repository.TrackingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class TrackingDataModule {
    @Binds
    @Singleton
    abstract fun bindRouteRepository(impl: RouteRepositoryImpl): RouteRepository

    @Binds
    @Singleton
    abstract fun bindAddressRepository(impl: AddressRepositoryImpl): AddressRepository

    @Binds
    @Singleton
    abstract fun bindTrackingRepository(impl: TrackingRepositoryImpl): TrackingRepository
}
