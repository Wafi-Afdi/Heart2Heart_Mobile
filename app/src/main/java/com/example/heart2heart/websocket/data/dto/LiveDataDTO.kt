package com.example.heart2heart.websocket.data.dto

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class LiveDataDTO(
    val ecgList: List<Float>,
    val start: String,
    val bpm: Int,
)
