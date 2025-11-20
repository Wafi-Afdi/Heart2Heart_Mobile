package com.example.heart2heart.websocket.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserWebSocketDTO(
    val name: String,
    val email: String,
    val isDisconnecting: Boolean,
    val isAmbulatory: Boolean,
    val isBluetoothConnected: Boolean,
)
