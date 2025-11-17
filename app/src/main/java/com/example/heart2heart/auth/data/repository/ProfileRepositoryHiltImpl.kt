package com.example.heart2heart.auth.data.repository

import android.content.Context
import arrow.core.Either
import com.example.heart2heart.auth.data.AppType
import com.example.heart2heart.auth.data.UserProfile
import com.example.heart2heart.auth.data.remote.AuthAPI
import com.example.heart2heart.auth.data.remote.ProfileAPI
import com.example.heart2heart.auth.domain.model.ListOfContactModelResponse
import com.example.heart2heart.auth.repository.ProfileRepository
import com.example.heart2heart.common.data.database.AppDB
import com.example.heart2heart.common.data.toGeneralError
import com.example.heart2heart.common.domain.model.NetworkError
import com.example.heart2heart.common.domain.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import okhttp3.ResponseBody
import java.util.UUID
import javax.inject.Inject

class ProfileRepositoryHiltImpl@Inject constructor(
    private val profileAPI: ProfileAPI,
    @ApplicationContext private val context: Context,

): ProfileRepository {
    private val _userData = MutableStateFlow<UserProfile>(UserProfile(
        id = "",
        name = "",
        email = "",
        phoneNumber = "",
        role = "",
    ))

    override val userData: StateFlow<UserProfile>
        get() = _userData.asStateFlow()

    private val _ecgObserving = MutableStateFlow<UserProfile>(
        UserProfile(
            id = "",
            name = "",
            email = "",
            phoneNumber = "",
            role = "",
        )
    )

    override val ECGObserving: StateFlow<UserProfile>
        get() = _ecgObserving.asStateFlow()

    private val _appType = MutableStateFlow<AppType?>(null)
    override val appType: StateFlow<AppType?>
        get() = _appType.asStateFlow()

    override fun setUserProfile(userProfile: UserProfile) {
        _userData.update {
            it.copy(
                id = userProfile.id,
                email = userProfile.email,
                phoneNumber = userProfile.phoneNumber,
                name = userProfile.name,
                role = userProfile.role
            )

        }
    }

    override fun setECGObserving(userProfile: UserProfile) {
        _ecgObserving.update {
            it.copy(
                id = userProfile.id,
                email = userProfile.email,
                phoneNumber = userProfile.phoneNumber,
                name = userProfile.name,
                role = userProfile.role
            )
        }
    }

    override fun setAppType(appType: AppType) {
        _appType.update {
            appType
        }
    }

    override suspend fun getUserProfile(): Either<NetworkError, UserProfile> {
        return Either.catch {
            profileAPI.getUserDataAPI()
        }.mapLeft {
            it.toGeneralError()
        }
    }

    override suspend fun getListOfContact(): Either<NetworkError, ListOfContactModelResponse> {
        return Either.catch {
            profileAPI.getListOfContacts()
        }.mapLeft {
            it.toGeneralError()
        }
    }


    override suspend fun verifyUser(): Either<NetworkError, String> {
        return Either.catch {
            val responseBody: ResponseBody = profileAPI.verifyUserAPI()

            // Convert the ResponseBody to a String
            responseBody.string()
        }.mapLeft {
            it.toGeneralError()
        }
    }
}