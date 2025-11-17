package com.example.heart2heart.auth.data.repository

import arrow.core.Either
import com.example.heart2heart.auth.data.LoginRequest
import com.example.heart2heart.auth.data.LoginResponse
import com.example.heart2heart.auth.data.RegisterRequest
import com.example.heart2heart.auth.data.UserProfile
import com.example.heart2heart.auth.data.remote.AuthAPI
import com.example.heart2heart.auth.repository.AuthRepository
import com.example.heart2heart.common.data.toGeneralError
import com.example.heart2heart.common.domain.model.NetworkError
import android.content.Context
import android.content.SharedPreferences
import arrow.core.const
import com.example.heart2heart.auth.data.AppType
import com.example.heart2heart.auth.data.RegisterResponse
import com.example.heart2heart.auth.data.remote.ProfileAPI
import com.example.heart2heart.common.data.database.AppDB
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class AuthRepositoryHiltImpl @Inject constructor(
    private val authAPI: AuthAPI,
    @ApplicationContext private val context: Context,

): AuthRepository {
    private val _preference: SharedPreferences = context.getSharedPreferences("auth_pref", Context.MODE_PRIVATE)

    override suspend fun registerUser(registerData: RegisterRequest): Either<NetworkError, RegisterResponse> {
        return Either.catch {
            authAPI.registerAPI(registerData)
        }.mapLeft {
            it.toGeneralError()
        }
    }

    override suspend fun loginUser(loginData: LoginRequest): Either<NetworkError, LoginResponse> {
        return Either.catch {
            authAPI.loginAPI(loginData)
        }.mapLeft {
            it.toGeneralError()
        }
    }



    override fun getUserToken(): String? {
        return _preference.getString("auth_token", null)
    }

    override fun setUserToken(token: String) {
        val editor = _preference.edit()
        editor.putString("auth_token", token)
        editor.apply()
    }

    override fun logoutUser() {
        val editor = _preference.edit()
        editor.remove("auth_token")
        editor.apply()
    }


}