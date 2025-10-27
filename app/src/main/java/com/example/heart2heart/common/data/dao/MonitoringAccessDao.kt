package com.example.heart2heart.common.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.heart2heart.common.domain.model.MonitoringAccess
import com.example.heart2heart.common.domain.model.User
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface MonitoringAccessDao {

    @Update
    suspend fun updateMonitoringAccess(monitoringAccess: MonitoringAccess)

    @Query("SELECT * FROM monitoringAccess WHERE resourceOwnerID = :id")
    suspend fun getMonitoringAccessByOwnerID(id: UUID): List<MonitoringAccess>

    @Query("SELECT * FROM monitoringAccess WHERE watcherUserID = :id")
    suspend fun getMonitoringAccessByWatcherID(id: UUID): List<MonitoringAccess>

    @Delete
    suspend fun deleteMonitoringAccess(monitoringAccess: MonitoringAccess)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMonitoringAccess(monitoringAccess: MonitoringAccess)
}