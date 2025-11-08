package com.example.heart2heart.auth.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heart2heart.auth.data.LoginRequest
import com.example.heart2heart.auth.data.RegisterRequest
import com.example.heart2heart.auth.repository.AuthRepository
import com.example.heart2heart.common.domain.model.ApiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepo: AuthRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUIState())

    val uiState = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun onPhoneUpdate(phone: String) {
        _uiState.update { it.copy(phone = phone) }
    }

    fun onFullNameUpdate(fullName: String) {
        _uiState.update { it.copy(fullName = fullName) }
    }

    fun register() {
        if (_uiState.value.isLoading) return

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val request = RegisterRequest(
                email = _uiState.value.email,
                password = _uiState.value.password,
                phone = _uiState.value.phone,
                fullName = _uiState.value.fullName
            )

            authRepo.registerUser(request).fold(
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
                            isLoading = false,
                            error = errorMessage
                        )
                    }
                },
                ifRight = {
                    response ->
                    Log.i("LOGINSYSTEM", "token: ${response.message}")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            registerSuccess = true
                        )
                    }
                }
            )
        }

    }


    fun consumedRegisterSuccess() {
        _uiState.update { it.copy(registerSuccess = false) }
    }
}