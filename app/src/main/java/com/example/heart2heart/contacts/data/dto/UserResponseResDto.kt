package com.example.heart2heart.contacts.data.dto

import java.util.UUID

data class UserResponseResDto(
    val userId: UUID,
    val phone: String,
    val email: String,
    val name: String,
)
