package com.example.heart2heart.auth.presentation

data class RegisterUIState(
    val email: String = "",
    val password: String = "",
    val phone: String = "",
    val fullName: String = "",
    val isLoading: Boolean = false,
    val registerSuccess: Boolean = false,
    val error: String? = null
)
