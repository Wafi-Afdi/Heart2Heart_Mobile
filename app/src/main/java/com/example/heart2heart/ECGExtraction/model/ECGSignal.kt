package com.example.heart2heart.ECGExtraction.model

import java.time.LocalDateTime

data class ECGSignalDataSTM (
    val signal: Float,
    val recordTime: LocalDateTime,
    val intervalTime: Long,
    val RRPeak: Boolean?,
    val asystole: Boolean? = false,
)

fun ECGSignalDataSTM.toECGSignalModel(): ECGSignalDataModal {
    return ECGSignalDataModal(
        signal = signal,
        recordTime = recordTime,
        RRPeak = RRPeak ?: false,
    )
}