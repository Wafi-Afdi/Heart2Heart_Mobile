package com.example.heart2heart.auth.repository

import arrow.core.Either
import com.example.heart2heart.auth.data.AppType
import com.example.heart2heart.auth.data.LoginRequest
import com.example.heart2heart.auth.data.LoginResponse
import com.example.heart2heart.auth.data.RegisterRequest
import com.example.heart2heart.auth.data.RegisterResponse
import com.example.heart2heart.auth.data.UserProfile
import com.example.heart2heart.common.domain.model.NetworkError
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {

    suspend fun registerUser(registerData: RegisterRequest): Either<NetworkError, RegisterResponse>

    suspend fun loginUser(loginData: LoginRequest): Either<NetworkError, LoginResponse>



    fun getUserToken(): String?

    fun setUserToken(token: String)

    fun logoutUser(): Unit
}