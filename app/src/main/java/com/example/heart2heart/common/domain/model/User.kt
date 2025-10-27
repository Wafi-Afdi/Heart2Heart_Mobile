package com.example.heart2heart.common.domain.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(
    tableName = "user",
    indices = [
        Index(value = ["email"], unique = true),
        Index(value = ["phoneNumber"], unique = true)
    ]
)
data class User(
    @PrimaryKey val id: UUID = UUID.fromString(DEFAULT_UUID),
    val email: String,
    val tanggalLahir: Date?,
    val jenisKelamin: Int?,
    val firstName: String?,
    val lastName: String?,
    val phoneNumber: String?
) {
    companion object {
        const val DEFAULT_UUID = "00000000-0000-0000-0000-000000000000"
    }
}
