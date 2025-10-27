package com.example.heart2heart.common.data.converter

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.UUID

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromUuid(uuid: UUID?): String? {
        return uuid?.toString()
    }

    @TypeConverter
    fun toUuid(uuidString: String?): UUID? {
        return uuidString?.let { UUID.fromString(it) }
    }

    @TypeConverter
    fun fromTimestampToLocalDateTime(value: Long?): LocalDateTime? {
        return value?.let {
            Instant.ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
        }
    }

    @TypeConverter
    fun localDateTimeToTimestamp(date: LocalDateTime?): Long? {
        return date?.let {
            it.atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        }
    }
}