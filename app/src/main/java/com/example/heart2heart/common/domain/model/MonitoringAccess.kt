package com.example.heart2heart.common.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import java.util.UUID

@Entity(
    tableName = "monitoringAccess",
    primaryKeys = ["resourceOwnerID", "watcherUserID"],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["watcherUserID"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["resourceOwnerID"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class MonitoringAccess (
    val resourceOwnerID: UUID,
    val watcherUserID: UUID,
    val ecgAccess: Boolean? = false,
    val reportAccess: Boolean? = false,
    val locationAccess: Boolean? = false,
)