package com.example.heart2heart.auth.repository

import arrow.core.Either
import com.example.heart2heart.auth.data.AppType
import com.example.heart2heart.auth.data.UserProfile
import com.example.heart2heart.common.domain.model.NetworkError
import kotlinx.coroutines.flow.StateFlow

interface ProfileRepository {
    val userData: StateFlow<UserProfile>
    val appType: StateFlow<AppType?>

    suspend fun getUserProfile(): Either<NetworkError, UserProfile>

    suspend fun verifyUser(): Either<NetworkError, String>
    fun setUserProfile(userProfile: UserProfile)

    fun setAppType(appType: AppType)
}