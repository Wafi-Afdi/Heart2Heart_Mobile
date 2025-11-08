package com.example.heart2heart.common.data

import com.example.heart2heart.common.domain.model.ApiError
import com.example.heart2heart.common.domain.model.ApiErrorMessage
import com.example.heart2heart.common.domain.model.NetworkError
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException

fun Throwable.toGeneralError(): NetworkError {
    var message: String? = ""
    val error = when (this) {
        is IOException -> ApiError.NetworkError
        is HttpException -> {
            val errorBodyString = this.response()?.errorBody()?.string().runCatching {
                this
            }.getOrNull()
            message = errorBodyString?.let { rawString ->
                try {
                    val parsedError = Gson().fromJson(rawString, ApiErrorMessage::class.java)
                    parsedError.message
                } catch (e: Exception) {
                    rawString
                }
            }
            when (this.code()) {
                400 -> ApiError.BadRequest
                401 -> ApiError.Unauthorized
                404 -> ApiError.NotFound
                in 500..599 -> ApiError.ServerError
                else -> ApiError.UnknownResponse
            }
        }
        else -> ApiError.UnknownError
    }
    return NetworkError(
        error = error,
        t = this,
        message = message
    )
}