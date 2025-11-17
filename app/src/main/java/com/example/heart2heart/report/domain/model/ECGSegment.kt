package com.example.heart2heart.report.domain.model

import java.time.LocalDateTime

data class ECGSegment(
    val signal: Float,
    val timestamp: LocalDateTime,
)
