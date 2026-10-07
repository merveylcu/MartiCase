package com.merveylcu.marticase.core.database.di

import android.content.Context
import androidx.room.Room
import com.merveylcu.marticase.core.database.MartiCaseDatabase
import com.merveylcu.marticase.core.database.dao.RoutePointDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DATABASE_NAME = "marticase.db"

@Module
@InstallIn(SingletonComponent::class)
public object DatabaseModule {
    @Provides
    @Singleton
    public fun provideDatabase(@ApplicationContext context: Context): MartiCaseDatabase = Room
        .databaseBuilder(
            context,
            MartiCaseDatabase::class.java,
            DATABASE_NAME,
        ).build()

    @Provides
    public fun provideRoutePointDao(database: MartiCaseDatabase): RoutePointDao =
        database.routePointDao()
}
