package com.example.heart2heart.websocket.repository

import com.example.heart2heart.auth.data.AppType
import com.example.heart2heart.common.domain.model.User
import com.example.heart2heart.websocket.data.dto.EventArrhythmiaDTO
import com.example.heart2heart.websocket.data.dto.LiveDataDTO
import com.example.heart2heart.websocket.data.dto.LocationDTO
import com.example.heart2heart.websocket.data.dto.UserWebSocketDTO
import com.example.heart2heart.websocket.model.UserWebSocket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime

interface WebSocketRepository {
    val userFlow: Flow<UserWebSocketDTO>
    val statusFlow: Flow<String> // Flow for /topic/status updates (CONNECTED, DISCONNECTED, etc.)
    val liveDataFlow: Flow<LiveDataDTO> // Flow for /topic/liveData updates (client and server published)
    val notificationFlow: Flow<EventArrhythmiaDTO> // Flow for /topic/notification
    val locationFlow: Flow<LocationDTO> // Flow for /topic/location

    val isConnected: StateFlow<Boolean>
    val lastConnectionTime: StateFlow<LocalDateTime?>

    val connectedUser: StateFlow<Map<String, UserWebSocketDTO>>

    fun connect(user: UserWebSocket, appType: AppType)

    fun connect()

    fun disconnect()

    fun publish(destination: String, payload: String)

    fun publishBluetoothIsActive()
}