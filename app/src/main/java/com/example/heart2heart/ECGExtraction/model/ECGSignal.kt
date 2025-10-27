package com.example.heart2heart.ECGExtraction.model

import java.time.LocalDateTime

data class ECGSignalData (
    val signal: Double,
    val recordTime: LocalDateTime
)