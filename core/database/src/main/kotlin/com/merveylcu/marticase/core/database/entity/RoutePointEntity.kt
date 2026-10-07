package com.merveylcu.marticase.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "route_points")
public data class RoutePointEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val latitude: Double,
    val longitude: Double,
    val accuracyMeters: Float,
    val recordedAtMillis: Long,
    val address: String? = null,
)
