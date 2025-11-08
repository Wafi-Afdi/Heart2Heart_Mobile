package com.example.heart2heart.auth.data.remote

import com.example.heart2heart.BuildConfig
import com.example.heart2heart.auth.data.UserProfile
import okhttp3.ResponseBody
import retrofit2.http.GET

interface ProfileAPI {

    @GET("api/user/verify")
    suspend fun verifyUserAPI(): ResponseBody

    @GET("api/user/profile")
    suspend fun getUserDataAPI(): UserProfile

    companion object {
        const val BASE_URL = BuildConfig.BE_API_LOCAL
    }
}