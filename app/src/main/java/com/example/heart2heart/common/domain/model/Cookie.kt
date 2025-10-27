package com.example.heart2heart.common.domain.model

import androidx.room.Entity

@Entity(tableName = "cookie", primaryKeys = ["id"])
data class Cookie(
    val id: String,
    val value: String,
)
