package com.merveylcu.marticase.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.merveylcu.marticase.core.database.dao.RoutePointDao
import com.merveylcu.marticase.core.database.entity.RoutePointEntity

@Database(
    entities = [RoutePointEntity::class],
    version = 1,
    exportSchema = false,
)
public abstract class MartiCaseDatabase : RoomDatabase() {
    public abstract fun routePointDao(): RoutePointDao
}
