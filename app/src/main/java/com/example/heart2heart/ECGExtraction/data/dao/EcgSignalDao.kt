package com.example.heart2heart.ECGExtraction.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.heart2heart.ECGExtraction.model.ECGSignalDataModal

@Dao
interface EcgSignalDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertBatch(signals: List<ECGSignalDataModal>)
}