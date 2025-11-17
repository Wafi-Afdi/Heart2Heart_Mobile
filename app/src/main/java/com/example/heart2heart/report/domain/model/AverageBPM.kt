package com.example.heart2heart.report.domain.model

import java.time.LocalDateTime

data class AverageBPM(
    val bpm: Float,
    val timestamp: LocalDateTime
)
