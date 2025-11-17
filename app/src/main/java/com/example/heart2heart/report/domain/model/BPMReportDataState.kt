package com.example.heart2heart.report.domain.model

data class BPMReportDataState(
    val label: String,
    val dateMillis: Long,
    val highest: Float,
    val lowest: Float,
    val average: Float,

)
