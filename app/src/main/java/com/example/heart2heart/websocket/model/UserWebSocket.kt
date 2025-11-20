package com.example.heart2heart.websocket.model

data class UserWebSocket(
    val username: String,
    val name: String,
    val jwtToken: String // The raw token string (without "Bearer ")
)
