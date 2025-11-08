package com.example.heart2heart.auth.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heart2heart.auth.data.LoginRequest
import com.example.heart2heart.auth.repository.AuthRepository
import com.example.heart2heart.auth.repository.ProfileRepository
import com.example.heart2heart.common.domain.model.ApiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val profileRepo: ProfileRepository,
): ViewModel() {
    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun login() {
        // Don't start another login if one is already in progress
        if (_uiState.value.isLoading) return

        // Set loading state
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val request = LoginRequest(
                email = _uiState.value.email,
                password = _uiState.value.password
            )

            authRepo.loginUser(request).fold(
                ifLeft = { networkError ->
                    val errorMessage = when (networkError.error) {
                        ApiError.NetworkError -> "No internet connection. Please try again."
                        ApiError.Unauthorized -> "Invalid email or password."
                        ApiError.BadRequest -> "There was an issue with your request."
                        ApiError.NotFound -> "User not found."
                        ApiError.ServerError -> "Server error. Please try again later."
                        else -> "An unknown error occurred."
                    }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = errorMessage
                        )
                    }
                },
                ifRight = { loginResponse ->
                    authRepo.setUserToken(loginResponse.accessToken)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            loginSuccess = true
                        )
                    }
                }
            )
        }

    }

    fun checkAuth() {
        viewModelScope.launch {
            profileRepo.verifyUser().fold(
                ifLeft = {
                        networkError ->
                    val errorMessage = when (networkError.error) {
                        ApiError.NetworkError -> "No internet connection. Please try again."
                        ApiError.Unauthorized -> "Invalid email or password."
                        ApiError.BadRequest -> "There was an issue with your request."
                        ApiError.NotFound -> "User not found."
                        ApiError.ServerError -> "Server error. Please try again later."
                        else -> "An unknown error occurred."
                    }
                    _uiState.update {
                        it.copy(
                            checkAuth = false
                        )
                    }
                },
                ifRight = {
                    _uiState.update {
                        it.copy(
                            checkAuth = true
                        )
                    }
                }
            )
        }
    }

    fun consumedLoginSuccess() {
        _uiState.update { it.copy(loginSuccess = false) }
    }

    fun consumeCheckAuthSuccess() {
        _uiState.update { it.copy(checkAuth = false) }
    }

    fun consumedError() {
        _uiState.update { it.copy(error = null) }
    }
}