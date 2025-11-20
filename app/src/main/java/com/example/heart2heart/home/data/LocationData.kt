package com.example.heart2heart.home.data

import android.location.Location

data class LocationData(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val accuracy: Float = 0.0f,
    val altitude: Double? = 0.0,
    val speed: Float? = 0.0f,
    val bearing: Float? = 0.0f,
    val provider: String = "",
    val time: String = "",
    var city: String? = "-",
    var country: String? = "-",
) {
    companion object {
        fun fromLocation(location: Location): LocationData {
            return LocationData(
                latitude = location.latitude,
                longitude = location.longitude,
                accuracy = location.accuracy,
                altitude = if (location.hasAltitude()) location.altitude else null,
                speed = if (location.hasSpeed()) location.speed else null,
                bearing = if (location.hasBearing()) location.bearing else null,
                provider = location.provider ?: "Unknown",
                time = java.text.SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss",
                    java.util.Locale.getDefault()
                ).format(java.util.Date(location.time))
            )
        }
    }
}