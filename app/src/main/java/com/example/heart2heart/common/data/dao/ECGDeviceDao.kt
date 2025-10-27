package com.example.heart2heart.common.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.heart2heart.common.domain.model.ECGDevice
import com.example.heart2heart.common.domain.model.User
import java.util.UUID

@Dao
interface ECGDeviceDao {
    @Query("SELECT * FROM ecgDevice WHERE deviceID = :id")
    suspend fun getECGByBluetoothID(id: String): ECGDevice?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertECGDevice(ecgDevice: ECGDevice)

    @Update
    suspend fun updateECGDeviceData(ecgDevice: ECGDevice)

    @Delete
    suspend fun deleteECGDevice(ecgDevice: ECGDevice)
}