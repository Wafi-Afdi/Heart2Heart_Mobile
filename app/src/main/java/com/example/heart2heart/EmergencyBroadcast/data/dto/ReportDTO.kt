package com.example.heart2heart.EmergencyBroadcast.data.dto

import java.time.LocalDateTime

data class ReportDTO(
    val reportType: String,
    val timestamp: String,
    val ecgSegment: EcgSegmentDTO,
    val reportId: String,
)