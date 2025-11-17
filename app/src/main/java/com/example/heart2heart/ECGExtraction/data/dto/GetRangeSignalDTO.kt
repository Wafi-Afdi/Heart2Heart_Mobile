package com.example.heart2heart.ECGExtraction.data.dto

data class GetRangeSignalDTO(
    val start: String,
    val end: String,
    val userId: String?,
)
