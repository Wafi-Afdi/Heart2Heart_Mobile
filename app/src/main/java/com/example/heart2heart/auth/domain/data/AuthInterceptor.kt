package com.example.heart2heart.auth.domain.data

import com.example.heart2heart.auth.repository.AuthRepository
import jakarta.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor @Inject constructor(
    private val authRepository: AuthRepository // Assumes you have a repo to get the token
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val token = authRepository.getUserToken()

        val newRequestBuilder = originalRequest.newBuilder()


        if (token != null) {
            newRequestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(newRequestBuilder.build())

    }
}