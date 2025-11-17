package com.example.heart2heart.contacts.data.repository

import android.content.Context
import arrow.core.Either
import com.example.heart2heart.EmergencyBroadcast.data.remote.ReportAPI
import com.example.heart2heart.auth.domain.model.ListOfContactModelResponse
import com.example.heart2heart.common.data.toGeneralError
import com.example.heart2heart.common.domain.model.NetworkError
import com.example.heart2heart.contacts.data.dto.AddUserAccesDTO
import com.example.heart2heart.contacts.data.dto.UserResponseResDto
import com.example.heart2heart.contacts.data.remote.ContactAPI
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject

class ContactRepository @Inject constructor(
    private val contactAPI: ContactAPI
) {
    suspend fun getUserContacts(isAmbulatory: Boolean): Either<NetworkError, ListOfContactModelResponse> {
        return Either.catch {
            contactAPI.getListOfContacts(isAmbulatory)
        }.mapLeft {
            it.toGeneralError()
        }
    }

    suspend fun addUserContact(email: String): Either<NetworkError, UserResponseResDto> {
        return Either.catch {
            contactAPI.addContact(addUserAccesDTO = AddUserAccesDTO(email = email))
        }.mapLeft {
            it.toGeneralError()
        }
    }

    suspend fun deleteUserContact(id: UUID): Either<NetworkError, String> {
        return Either.catch {
            contactAPI.deleteContact(id)
        }.mapLeft {
            it.toGeneralError()
        }
    }
}