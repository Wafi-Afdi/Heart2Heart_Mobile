package com.example.heart2heart.websocket.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heart2heart.auth.data.AppType
import com.example.heart2heart.auth.repository.AuthRepository
import com.example.heart2heart.auth.repository.ProfileRepository
import com.example.heart2heart.websocket.data.dto.LiveDataDTO
import com.example.heart2heart.websocket.data.dto.LocationDTO
import com.example.heart2heart.websocket.data.repository.WebsocketImpl
import com.example.heart2heart.websocket.model.UserWebSocket
import com.example.heart2heart.websocket.repository.WebSocketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import java.time.LocalDateTime
import javax.inject.Inject


@HiltViewModel
class TestWSViewModel @Inject constructor(
    private val webSocketRepository: WebSocketRepository,
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository
)
: ViewModel() {
    fun connect() {
        webSocketRepository.connect(
            UserWebSocket(
                username = profileRepository.userData.value.email,
                name = profileRepository.userData.value.name,
                jwtToken = authRepository.getUserToken() ?: ""
            ),
            profileRepository.appType.value ?: AppType.AMBULATORY
        )
    }

    fun sendMessage() {
        val newLiveData = LiveDataDTO(
            ecgList = emptyList(),
            start = LocalDateTime.now().toString(),
            bpm = 0
        )
        val newLocationData = LocationDTO(
            country = "Indonesia",
            city = "Batam",
            longitude = 1.0f,
            lat = 1.0f,
        )
        viewModelScope.launch {

            webSocketRepository.publish("/app/liveData", Json.encodeToString(newLiveData))

            delay(1000)
            webSocketRepository.publish("/app/location", Json.encodeToString(newLocationData))
        }
    }

    fun disconnect() {
        webSocketRepository.disconnect()
    }
}