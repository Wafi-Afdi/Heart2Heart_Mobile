package com.example.heart2heart.ECGExtraction.data.dto

import java.time.LocalDateTime

data class EcgDataDTO(
    val signal: Float,
    val rp: Boolean,
    val flat: Boolean,
    val ts: String,
)