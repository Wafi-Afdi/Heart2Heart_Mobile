package com.example.heart2heart.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heart2heart.auth.domain.AuthState
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
class AuthCheckViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val profileRepo: ProfileRepository,
): ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState = _authState.asStateFlow()

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
                    _authState.update {
                        AuthState.Unauthenticated
                    }
                },
                ifRight = {
                    _authState.update {
                        AuthState.Authenticated
                    }
                }
            )
        }
    }

    fun logOut() {
        authRepo.logoutUser()
    }
}