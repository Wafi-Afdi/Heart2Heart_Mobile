package com.example.heart2heart.contacts.presentation

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heart2heart.auth.data.AppType
import com.example.heart2heart.auth.domain.model.ContactModel
import com.example.heart2heart.auth.repository.ProfileRepository
import com.example.heart2heart.common.domain.model.ApiError
import com.example.heart2heart.contacts.data.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

@HiltViewModel
class ContactViewModel
@Inject constructor(
    private val contactRepository: ContactRepository,
    private val profileRepo: ProfileRepository,
    @ApplicationContext private val context: Context
): ViewModel() {

    private val _isLoadingContacts = MutableStateFlow(false)
    val isLoadingContacts: StateFlow<Boolean>
        get() = _isLoadingContacts.asStateFlow()

    private val _isLoadingAddContact = MutableStateFlow(false)
    val isLoadingAddContact: StateFlow<Boolean>
        get() = _isLoadingAddContact.asStateFlow()

    private val _isLoadingDeleteContact = MutableStateFlow(false)
    val isLoadingDeleteContact: StateFlow<Boolean>
        get() = _isLoadingDeleteContact.asStateFlow()

    private val _listOfContacts = MutableStateFlow<List<ContactModel>>(emptyList())
    val listOfContacts: StateFlow<List<ContactModel>>
        get() = _listOfContacts.asStateFlow()

    fun getListOfContacts() {
        viewModelScope.launch {
            _isLoadingContacts.update { true }
            contactRepository.getUserContacts(isAmbulatory = true).fold(
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

    fun addContact(email: String) {
        viewModelScope.launch {
            _isLoadingAddContact.update { true }
            contactRepository.addUserContact(email).fold(
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
                    Toast.makeText(context, "Contact added", Toast.LENGTH_SHORT).show()
                    getListOfContacts()
                }
            )
            _isLoadingAddContact.update { false }
        }
    }

    fun deleteContact(userId: UUID) {
        viewModelScope.launch {
            _isLoadingAddContact.update { true }
            contactRepository.deleteUserContact(userId).fold(
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
                    Toast.makeText(context, "Contact removed", Toast.LENGTH_SHORT).show()
                    _listOfContacts.update {
                        current ->
                        current.filter { user -> user.userId == userId }
                    }
                }
            )
            _isLoadingAddContact.update { false }
        }
    }
}