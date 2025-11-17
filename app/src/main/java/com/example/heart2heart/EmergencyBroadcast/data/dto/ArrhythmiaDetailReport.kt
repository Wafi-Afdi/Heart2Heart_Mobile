package com.example.heart2heart.EmergencyBroadcast.data.dto

import com.example.heart2heart.auth.data.dto.UserProfileResponseDTO

data class ArrhythmiaDetailReport(
    val isEmpty: Boolean,
    val report: ReportDTO,
    val userData: UserProfileResponseDTO
)
