package com.example.heart2heart.EmergencyBroadcast.data.dto

import java.time.LocalDateTime

data class ReportSosDto(
    val ts: String,
    val lat: Float,
    val longitude: Float,
)
