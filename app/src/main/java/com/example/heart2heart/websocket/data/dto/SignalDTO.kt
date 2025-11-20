package com.example.heart2heart.websocket.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignalDTO(
    val signal: Float,
    val ts: String,
)
