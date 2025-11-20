package com.example.heart2heart.EmergencyBroadcast.data.dto

import java.time.LocalDateTime

data class EcgSegmentDTO(
    val userId: String,
    val signal: List<Float>,
    val start: String,
    val end: String,
)
