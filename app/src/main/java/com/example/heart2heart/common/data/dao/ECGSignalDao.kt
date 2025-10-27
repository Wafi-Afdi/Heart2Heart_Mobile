package com.example.heart2heart.common.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.heart2heart.common.domain.model.ECGDevice
import com.example.heart2heart.common.domain.model.ECGSignal
import kotlinx.coroutines.flow.Flow
import java.util.Date
import java.util.UUID

@Dao
interface ECGSignalDao {
    @Query("SELECT * FROM ecgSignal WHERE userID = :id")
    fun getSignalDataByUserID(id: UUID): Flow<List<ECGSignal>>

    @Query("DELETE FROM ecgSignal WHERE userID = :id")
    suspend fun deleteECGSignalByUser(id: UUID)

    @Query("DELETE FROM ecgSignal WHERE userID = :id AND recordedDateTime BETWEEN :start AND :end")
    suspend fun deleteECGSignalByUserAndRange(id: UUID, start: Date, end: Date)

    @Query("SELECT * FROM ecgSignal WHERE userID = :id AND recordedDateTime BETWEEN :start AND :end")
    fun getSignalDataByUserIDAndRange(id: UUID, start: Date, end: Date): Flow<List<ECGSignal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertECGSignal(ecgSignal: ECGSignal)

    @Delete
    suspend fun deleteECGSignal(ecgSignal: ECGSignal)
}