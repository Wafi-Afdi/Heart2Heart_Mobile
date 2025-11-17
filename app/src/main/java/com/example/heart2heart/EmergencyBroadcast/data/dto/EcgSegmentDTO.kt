package com.example.heart2heart.EmergencyBroadcast.data.dto

import java.time.LocalDateTime

data class EcgSegmentDTO(
    val userId: String,
    val signal: Float,
    val asystole: Boolean,
    val rPeak: Boolean,
    val ts: String,
)
