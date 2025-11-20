package com.example.heart2heart.websocket.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LocationDTO(
    var lat: Float,
    var longitude: Float,
    var city: String,
    var country: String,
)
