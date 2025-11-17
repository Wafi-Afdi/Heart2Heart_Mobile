package com.example.heart2heart.EmergencyBroadcast.domain.data

import com.example.heart2heart.ECGExtraction.data.DetectionType


sealed interface EmergencyEvent {
    object EmergencySent : EmergencyEvent
    data class EmergencySOSObserver(val username: String): EmergencyEvent
    data class EmergencyReportObserver(val reportType: DetectionType, val username: String): EmergencyEvent
    data class EmergencyReportAmbulatory(val reportType: DetectionType): EmergencyEvent
}