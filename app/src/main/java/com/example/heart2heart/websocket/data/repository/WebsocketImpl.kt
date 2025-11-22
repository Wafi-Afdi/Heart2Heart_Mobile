package com.example.heart2heart.websocket.data.repository

import android.R
import android.util.Log
import com.example.heart2heart.BuildConfig
import com.example.heart2heart.auth.data.AppType
import com.example.heart2heart.auth.repository.AuthRepository
import com.example.heart2heart.auth.repository.ProfileRepository
import com.example.heart2heart.bluetooth.BluetoothServiceECG
import com.example.heart2heart.websocket.data.dto.EventArrhythmiaDTO
import com.example.heart2heart.websocket.data.dto.LiveDataDTO
import com.example.heart2heart.websocket.data.dto.LocationDTO
import com.example.heart2heart.websocket.data.dto.UserStatusDTO
import com.example.heart2heart.websocket.data.dto.UserWebSocketDTO
import com.example.heart2heart.websocket.model.UserWebSocket
import com.example.heart2heart.websocket.repository.WebSocketRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.isActive
import kotlinx.serialization.encodeToString
import javax.inject.Inject

class WebsocketImpl @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository,
    private val bluetoothServiceECG: BluetoothServiceECG,
): WebSocketRepository {

    companion object {
        private val WS_URL = BuildConfig.WS_API
        private val DOMAIN_BE = BuildConfig.DOMAIN_BE
    }
    private var webSocket: WebSocket? = null
    private var connectionUser: UserWebSocket? = null


    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val _statusFlow = MutableSharedFlow<String>(replay = 1)
    override val statusFlow: SharedFlow<String> = _statusFlow

    private val _userFlow = MutableSharedFlow<UserWebSocketDTO>(replay = 1)
    override val userFlow: Flow<UserWebSocketDTO> = _userFlow

    private val _liveDataFlow = MutableSharedFlow<LiveDataDTO>(replay = 0)
    override val liveDataFlow: SharedFlow<LiveDataDTO> = _liveDataFlow

    private val _notificationFlow = MutableSharedFlow<EventArrhythmiaDTO>(replay = 0)
    override val notificationFlow: SharedFlow<EventArrhythmiaDTO> = _notificationFlow

    private val _locationFlow = MutableSharedFlow<LocationDTO>(replay = 0)
    override val locationFlow: SharedFlow<LocationDTO> = _locationFlow

    private var locationCollectionJob: Job? = null

    private val _isConnected = MutableStateFlow(false)
    override val isConnected: StateFlow<Boolean>
        get() = _isConnected.asStateFlow()

    private val _lastConnectionTime = MutableStateFlow<LocalDateTime?>(null)
    override val lastConnectionTime: StateFlow<LocalDateTime?>
        get() = _lastConnectionTime.asStateFlow()

    private var _connectedUser = MutableStateFlow<Map<String, UserWebSocketDTO>>(emptyMap())
    override val connectedUser: StateFlow<Map<String, UserWebSocketDTO>>
        get() = _connectedUser.asStateFlow()

    private var subscriptionIdCounter = 0

    private var appType = AppType.AMBULATORY

    private var heartbeatJob: Job? = null // For WS Ping

    init {
        repositoryScope.launch {

            bluetoothServiceECG.isConnected.collect {
                if(profileRepository.appType.value == AppType.AMBULATORY) {
                    publishBluetoothIsActive()
                }
            }
        }
    }

    private fun createConnectFrame(token: String): String {
        return "CONNECT\n" +
                "accept-version:1.2\n" +
                "host:$DOMAIN_BE\n" +
                "Authorization:Bearer $token\n" +
                "heart-beat:30000,30000\n" +
                "\n\u0000"
    }

    private fun createSubscribeFrame(destination: String): String {
        val id = ++subscriptionIdCounter
        return "SUBSCRIBE\n" +
                "id:$id\n" +
                "destination:$destination\n" +
                "\n\u0000"
    }

    private fun createSendFrame(destination: String, payload: String): String {
        return "SEND\n" +
                "destination:$destination\n" +
                "content-type:application/json\n" +
                "content-length:${payload.length}\n" +
                "\n" +
                payload +
                "\u0000"
    }

    override fun connect(user: UserWebSocket, appType: AppType) {
        connectionUser = user
        this.appType = appType

        // OkHttpClient setup (in a real app, this should be a singleton)
        val client = OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .pingInterval(30, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder().url(WS_URL).build()
        webSocket = client.newWebSocket(request, StompWebSocketListener())
        _lastConnectionTime.update { LocalDateTime.now() }
        _isConnected.update { true }
    }

    override fun connect() {
        connectionUser = UserWebSocket(
            username = profileRepository.userData.value.email,
            name = profileRepository.userData.value.name,
            jwtToken = authRepository.getUserToken() ?: "",
        )
        this.appType = profileRepository.appType.value ?: AppType.AMBULATORY

        val client = OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .pingInterval(30, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder().url(WS_URL).build()
        webSocket = client.newWebSocket(request, StompWebSocketListener())
        _lastConnectionTime.update { LocalDateTime.now() }
        _isConnected.update { true }
    }

    private fun startHeartbeat() {
        // Cancel any existing job to prevent duplicates
        heartbeatJob?.cancel()

        // LAYER 2: STOMP Heartbeat (Application level)
        heartbeatJob = repositoryScope.launch {
            while (isActive) {
                // Wait for 10 seconds
                delay(30000)
                // Send a "Keep Alive" new-line character required by STOMP
                // This tells the server "I am still here"
                webSocket?.send("\n")
            }
        }
    }

    private fun stopHeartbeat() {
        heartbeatJob?.cancel()
        heartbeatJob = null
    }

    override fun disconnect() {
        // Send STOMP DISCONNECT frame before closing the socket
        _connectedUser.value = emptyMap()
        webSocket?.send("DISCONNECT\n\n\u0000")
        webSocket?.close(1000, "User disconnected")
        webSocket = null
        connectionUser = null
        repositoryScope.launch {
            _statusFlow.emit("DISCONNECTED")
        }
        _lastConnectionTime.update { null}
        _isConnected.update { false }
    }

    override fun publish(destination: String, payload: String) {
        Log.i("Websocket", "sending to $destination with payload $payload")
        if(_isConnected.value) {
            val frame = createSendFrame(destination, payload)
            webSocket?.send(frame)
        } else {
            Log.i("Websocket", "user not yet connected to server")
        }
    }

    override fun publishBluetoothIsActive() {
        Log.i("Websocket", "sending to status with payload bluetooth active")
        val userWebSocketDto = UserWebSocketDTO(
            name = profileRepository.userData.value.name,
            email = profileRepository.userData.value.email,
            isBluetoothConnected = bluetoothServiceECG.isConnected.value,
            isDisconnecting = false,
            isAmbulatory = profileRepository.appType.value == AppType.AMBULATORY,
        )
        if(_isConnected.value) {
            Log.i("Websocket", "sending to user publish info")
            val frame = createSendFrame("/app/user", Json.encodeToString(userWebSocketDto))
            webSocket?.send(frame)
        } else {
            Log.i("Websocket", "user not yet connected to server")
        }
    }

    // --- OkHttp WebSocket Listener Implementation ---
    private inner class StompWebSocketListener : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            val user = connectionUser ?: return disconnect()
            val connectFrame = createConnectFrame(user.jwtToken)
            webSocket.send(connectFrame)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            repositoryScope.launch {
                handleStompFrame(text)
            }
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            webSocket.close(1000, null)
            repositoryScope.launch {
                _statusFlow.emit("CLOSING: $reason")
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            // Handle failures like authentication errors during CONNECT
            stopHeartbeat()
            val errorMessage = "Error: ${t.message}"
            repositoryScope.launch {
                _statusFlow.emit(errorMessage)
                // Implement reconnection logic here if needed
                this@WebsocketImpl.webSocket = null
            }
            // Ensure socket is closed on failure
            webSocket.close(1000, "Failure")
        }

        // STOMP frame parser and dispatcher
        private fun handleStompFrame(frame: String) {
            val parts = frame.split("\n\n")
            if (parts.size < 2) return // Not a full STOMP frame

            val headerLines = parts[0].split('\n')
            val command = headerLines[0]
            val body = parts[1].trim('\u0000') // Remove null terminator

            when (command) {
                "CONNECTED" -> {
                    startHeartbeat()
                    Log.i("WebSocket", "status connected")
                    _statusFlow.tryEmit("CONNECTED")
                    // If connected, subscribe to all required topics
                    subscribeToTopics()
                }
                "MESSAGE" -> {
                    val destination = headerLines.find { it.startsWith("destination:") }?.substringAfter(":")?.trim()

                    // Dispatch the payload to the correct flow
                    when (destination) {
                        "/topic/status" -> {
                            val decodeUser = Json.decodeFromString<UserStatusDTO>(body)
                            Log.i("WebSocket", "status  $body")
                            if (decodeUser.email == profileRepository.ECGObserving.value.email && appType == AppType.OBSERVER) {
                                publishBluetoothIsActive()
                            } else if (decodeUser.email == profileRepository.userData.value.email && appType == AppType.AMBULATORY) {
                                Log.i("Websocket","Duplicated same user")
                            } else if (decodeUser.email == profileRepository.userData.value.email) {
                                Log.i("Websocket","Duplicated Observer ")
                            }
                            else if(!decodeUser.isDisconnecting) {
                                Log.i("Websocket","new user ${decodeUser.name}")
                                publishBluetoothIsActive()
                            } else if(decodeUser.isDisconnecting) {
                                Log.i("Websocket","remove user ${decodeUser.name}")
                                _connectedUser.update {
                                    current -> current.toMutableMap().apply {
                                        remove(decodeUser.email)
                                    }
                                }
                            }
                        }
                        "/topic/user" -> {
                            val decodeUser = Json.decodeFromString<UserWebSocketDTO>(body)
                            Log.i("WebSocket", "user  $body")
                            _connectedUser.update {
                                current -> current.toMutableMap().apply {
                                    if (decodeUser.email == profileRepository.ECGObserving.value.email && appType == AppType.OBSERVER) {
                                        return@apply
                                    } else if (decodeUser.email == profileRepository.userData.value.email && appType == AppType.AMBULATORY) {
                                        return@apply
                                    }
                                    else if(!decodeUser.isDisconnecting) {
                                        put(decodeUser.email,
                                            UserWebSocketDTO(
                                                name = decodeUser.name,
                                                isDisconnecting = false,
                                                email = decodeUser.email,
                                                isAmbulatory = decodeUser.isAmbulatory,
                                                isBluetoothConnected = decodeUser.isBluetoothConnected

                                            ))
                                    } else {
                                        remove(decodeUser.email)
                                    }
                                 }
                            }
                            repositoryScope.launch{ _userFlow.emit(decodeUser) }
                        }
                        "/topic/liveData" -> {
                            try {

                                Log.i("WebSocket", "liveData  $body")
                                val decodeLiveData = Json.decodeFromString<LiveDataDTO>(body)
                                repositoryScope.launch{ _liveDataFlow.emit(decodeLiveData)}
                            } catch (e: Exception) {
                                Log.e("Websocket", "failed parse $body")
                            }
                        }
                        "/topic/notification" -> {
                            Log.i("WebSocket", "notification  $body")
                            val decodeNotification = Json.decodeFromString<EventArrhythmiaDTO>(body)
                            repositoryScope.launch{ _notificationFlow.emit(decodeNotification)}

                        }
                        "/topic/location" -> {
                            Log.i("WebSocket", "location  $body")
                            val decodeLocation = Json.decodeFromString<LocationDTO>(body)
                            repositoryScope.launch {
                                _locationFlow.emit(decodeLocation)
                            }
                        }
                    }
                }
                "ERROR" -> {
                    Log.e("WebSocket", "status  disconnected")
                    repositoryScope.launch{ _statusFlow.emit("ERROR: $body")}
                    disconnect()
                }
            }
        }

        private fun subscribeToTopics() {
            if (this@WebsocketImpl.appType == AppType.AMBULATORY) {
                val subscribeFrame = createSubscribeFrame("/topic/status")
                val subscribeFrame2 = createSubscribeFrame("/topic/user")
                webSocket?.send(subscribeFrame)
                webSocket?.send(subscribeFrame2)
                return
            }
            val topics = listOf(
                "/topic/status",
                "/topic/user",
                "/topic/liveData",
                "/topic/notification",
                "/topic/location"
            )
            topics.forEach { topic ->
                val subscribeFrame = createSubscribeFrame(topic)
                webSocket?.send(subscribeFrame)
            }

            publishBluetoothIsActive()
        }
    }
}