package com.example.heart2heart.report.presentation.state

import java.time.LocalDateTime

data class rangeDate(
    val start: LocalDateTime,
    val end: LocalDateTime,
    val isLoading: Boolean =false,
)