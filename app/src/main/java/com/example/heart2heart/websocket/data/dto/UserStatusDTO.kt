package com.example.heart2heart.websocket.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserStatusDTO(
    val name: String,
    val email: String,
    val isDisconnecting: Boolean
)
