package com.example.heart2heart.EmergencyBroadcast.presentation.state

import com.example.heart2heart.ECGExtraction.data.DetectionType

data class EmergencyReportState(
    val type: DetectionType? = null,
    val username: String? = null,
    val isOpenDialog: Boolean,
    val isSOSObserverOpen: Boolean,
)
