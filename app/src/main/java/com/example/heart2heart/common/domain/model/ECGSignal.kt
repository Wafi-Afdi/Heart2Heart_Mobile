package com.example.heart2heart.common.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import java.time.LocalDateTime
import java.util.Date
import java.util.UUID

@Entity(
    tableName = "ecgSignal",
    primaryKeys = ["id", "ECGDeviceID", "userID"],
    foreignKeys = [
        ForeignKey(
            entity = ECGDevice::class,
            parentColumns = ["deviceID"],
            childColumns = ["ECGDeviceID"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userID"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["userID", "recordedDateTime"])
    ]
)
data class ECGSignal(
    val id: UUID = UUID.fromString(DEFAULT_UUID),
    val signal: Double,
    val recordedDateTime: LocalDateTime,
    @ColumnInfo(name = "userID") val userID: UUID,
    @ColumnInfo(name = "ECGDeviceID") val ECGDeviceID: String,
) {
    companion object {
        const val DEFAULT_UUID = "00000000-0000-0000-0000-000000000000"
    }
}
