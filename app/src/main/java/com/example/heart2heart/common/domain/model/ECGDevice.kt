package com.example.heart2heart.common.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.heart2heart.common.domain.model.User.Companion.DEFAULT_UUID
import java.time.LocalDateTime
import java.util.Date
import java.util.UUID

@Entity(
    tableName = "ecgDevice",
    primaryKeys = ["deviceID"]
)
data class ECGDevice(
    val deviceID: String,
    val location_langitude: Float?,
    val location_latitude: Float?,
    val deviceName: String?,
    val lastConnectedTime: LocalDateTime?,
    val firstConnectedTime: LocalDateTime?,
)
