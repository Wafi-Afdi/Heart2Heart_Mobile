package com.example.heart2heart.websocket.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class EventArrhythmiaDTO(
    val type: String,
    val name: String,
)
