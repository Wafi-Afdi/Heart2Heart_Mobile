package com.example.heart2heart.EmergencyBroadcast.presentation.state

import com.example.heart2heart.ECGExtraction.data.DetectionType
import com.example.heart2heart.ECGExtraction.data.ReportType
import com.example.heart2heart.report.domain.model.ECGSegment
import java.time.LocalDateTime
import java.util.UUID

data class ReportUIState(
    val username: String = "",
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val reportType: ReportType = ReportType.NORMAL,
    val ecgSegment: List<ECGSegment>? = null,
    val reportId: UUID = UUID.randomUUID(),
)
