package com.merveylcu.marticase.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.merveylcu.marticase.core.database.entity.RoutePointEntity
import kotlinx.coroutines.flow.Flow

@Dao
public interface RoutePointDao {
    @Query("SELECT * FROM route_points ORDER BY id")
    public fun observeAll(): Flow<List<RoutePointEntity>>

    @Query("SELECT * FROM route_points ORDER BY id DESC LIMIT 1")
    public suspend fun getLast(): RoutePointEntity?

    @Query("SELECT * FROM route_points WHERE id = :id")
    public suspend fun getById(id: Long): RoutePointEntity?

    @Insert
    public suspend fun insert(point: RoutePointEntity): Long

    @Query("UPDATE route_points SET address = :address WHERE id = :id")
    public suspend fun updateAddress(id: Long, address: String)

    @Query("DELETE FROM route_points")
    public suspend fun deleteAll()
}
