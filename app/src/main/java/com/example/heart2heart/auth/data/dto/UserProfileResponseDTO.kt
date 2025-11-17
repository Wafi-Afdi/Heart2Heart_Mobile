package com.example.heart2heart.auth.data.dto

import java.util.UUID

data class UserProfileResponseDTO(
    val id: UUID,
    val email: String,
    val name: String,
    val phoneNumber: String,
    val role: String,
)
