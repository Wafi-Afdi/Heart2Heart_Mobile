package com.example.heart2heart.auth.data

data class LoginRequest(
    val email: String,
    val password: String,
)

data class LoginResponse(
    val accessToken: String,
    val tokenType: String,
)
