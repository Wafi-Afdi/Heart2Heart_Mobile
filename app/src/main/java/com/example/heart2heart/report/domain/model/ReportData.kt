package com.example.heart2heart.report.domain.model

import com.example.heart2heart.ECGExtraction.data.DetectionType
import com.example.heart2heart.ECGExtraction.data.ReportType
import java.time.LocalDateTime
import java.util.UUID

data class ReportData(
    val detectionType: ReportType,
    val reportId: UUID,
    val userId: UUID,
    val ts: LocalDateTime,
    val username: String,
)
