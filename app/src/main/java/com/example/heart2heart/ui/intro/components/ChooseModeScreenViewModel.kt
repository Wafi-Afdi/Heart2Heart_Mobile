package com.example.heart2heart.ui.intro.components

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heart2heart.auth.data.AppType
import com.example.heart2heart.auth.data.UserProfile
import com.example.heart2heart.auth.domain.model.ContactModel
import com.example.heart2heart.auth.presentation.ChooseScreenUIState
import com.example.heart2heart.auth.presentation.LoginUIState
import com.example.heart2heart.auth.repository.AuthRepository
import com.example.heart2heart.auth.repository.ProfileRepository
import com.example.heart2heart.bluetooth.BluetoothDataModel
import com.example.heart2heart.common.domain.model.ApiError
import com.example.heart2heart.contacts.data.ContactUIState
import com.example.heart2heart.contacts.data.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooseModeScreenViewModel@Inject constructor(
    private val profileRepo: ProfileRepository,
    private val contactRepository: ContactRepository,
    @ApplicationContext private val context: Context
): ViewModel() {
    private val _uiState = MutableStateFlow(ChooseScreenUIState())
    val uiState = _uiState.asStateFlow()

    private val _listOfContacts = MutableStateFlow<List<ContactModel>>(emptyList())
    val listOfContacts: StateFlow<List<ContactModel>>
        get() = _listOfContacts.asStateFlow()

    private val _isLoadingContacts = MutableStateFlow(false)
    val isLoadingContacts: StateFlow<Boolean>
        get() = _isLoadingContacts.asStateFlow()

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

    fun setEcgObserving(userData: UserProfile) {
        profileRepo.setECGObserving(userData)
    }

    fun getListOfContacts() {
        viewModelScope.launch {
            _isLoadingContacts.update { true }
            contactRepository.getUserContacts(isAmbulatory = false).fold(
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
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                },
                ifRight = {
                    res ->
                    _listOfContacts.update {
                        res.contactList
                    }
                    _isLoadingContacts.update { false }
                }
            )
        }
    }
}