package com.example.heart2heart.ui.intro.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heart2heart.auth.data.AppType
import com.example.heart2heart.auth.presentation.ChooseScreenUIState
import com.example.heart2heart.auth.presentation.LoginUIState
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
class ChooseModeScreenViewModel@Inject constructor(
    private val profileRepo: ProfileRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(ChooseScreenUIState())
    val uiState = _uiState.asStateFlow()

    fun getUserData() {
        viewModelScope.launch {
            profileRepo.getUserProfile().fold(
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
                },
                ifRight = {
                    response ->
                    profileRepo.setUserProfile(response)
                    _uiState.update {
                        it.copy(
                            profileSuccess = true,
                            error = null
                        )
                    }

                }
            )
        }
    }

    fun setAppType(appType: AppType) {
        profileRepo.setAppType(appType)
    }
}