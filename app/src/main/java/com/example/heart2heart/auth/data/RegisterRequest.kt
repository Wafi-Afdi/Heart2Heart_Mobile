package com.example.heart2heart.auth.data

data class RegisterRequest(
    val email: String,
    val password: String,
    val fullName: String,
    val phone: String,
)

data class RegisterResponse(
    val message: String,
    val isSuccess: Boolean,
)
