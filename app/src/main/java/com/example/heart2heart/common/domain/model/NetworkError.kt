package com.example.heart2heart.common.domain.model

data class NetworkError(
    val error: ApiError,
    val t: Throwable? = null,
    val message: String? = null,
)

enum class ApiError {
    NetworkError,
    Unauthorized,
    BadRequest,
    NotFound,
    ServerError,
    UnknownResponse,
    UnknownError
}
