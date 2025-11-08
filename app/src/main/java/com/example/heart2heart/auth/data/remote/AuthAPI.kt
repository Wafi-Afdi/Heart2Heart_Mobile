package com.example.heart2heart.auth.data.remote


import android.service.autofill.UserData
import com.example.heart2heart.BuildConfig
import com.example.heart2heart.auth.data.LoginRequest
import com.example.heart2heart.auth.data.LoginResponse
import com.example.heart2heart.auth.data.RegisterRequest
import com.example.heart2heart.auth.data.RegisterResponse
import com.example.heart2heart.auth.data.UserProfile
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthAPI {

    @POST("api/auth/login")
    suspend fun loginAPI(
        @Body loginData: LoginRequest
    ): LoginResponse

    @POST("api/auth/register")
    suspend fun registerAPI(
        @Body registerData: RegisterRequest
    ): RegisterResponse

    companion object {
        const val BASE_URL = BuildConfig.BE_API_LOCAL
    }
}