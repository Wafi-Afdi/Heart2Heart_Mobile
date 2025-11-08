package com.example.heart2heart.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heart2heart.auth.presentation.RegisterUIState
import com.example.heart2heart.auth.repository.AuthRepository
import com.example.heart2heart.auth.repository.ProfileRepository
import com.example.heart2heart.home.presentation.state.UserHomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val profileRepo: ProfileRepository
): ViewModel() {
    private val _userHomeState = MutableStateFlow(UserHomeState())

    val userHomeState = _userHomeState.asStateFlow()

    init {
        viewModelScope.launch {
            _userHomeState.update { currentState ->
                currentState.copy(
                    appType = profileRepo.appType.value,
                    name = profileRepo.userData.value.name
                )
            }

            combine(
                profileRepo.appType,
                profileRepo.userData
            ) { appType, userData ->
                _userHomeState.update { currentState ->
                    currentState.copy(
                        appType = appType,
                        name = userData.name
                    )
                }
            }.collect()
        }
    }
}