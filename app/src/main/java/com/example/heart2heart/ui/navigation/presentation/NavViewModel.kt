package com.example.heart2heart.ui.navigation.presentation

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import com.example.heart2heart.ECGExtraction.domain.ObserverForegroundService
import com.example.heart2heart.auth.data.AppType
import com.example.heart2heart.auth.repository.ProfileRepository
import com.example.heart2heart.websocket.repository.WebSocketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class NavViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val profileRepo: ProfileRepository,
    private val webSocketRepository: WebSocketRepository,
): ViewModel() {

    val appType: StateFlow<AppType?>
        get() = profileRepo.appType

    val isConnectedWebSocket = webSocketRepository.isConnected

    fun startObserverService() {
        Intent(context, ObserverForegroundService::class.java).also {
            context.startService(it)
        }
    }

    fun stopObserverService() {
        Intent(context, ObserverForegroundService::class.java).also {
            context.stopService(it)
        }
    }
}