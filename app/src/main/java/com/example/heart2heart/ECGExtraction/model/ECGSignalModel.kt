package com.example.heart2heart.ECGExtraction.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "ecg_signals")
data class ECGSignalDataModal(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val signal: Float,
    val RRPeak: Boolean,
    val recordTime: LocalDateTime
)