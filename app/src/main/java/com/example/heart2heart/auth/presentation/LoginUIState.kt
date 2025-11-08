package com.example.heart2heart.auth.presentation

data class LoginUIState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false,
    val checkAuth: Boolean = false,
    val error: String? = null
)
