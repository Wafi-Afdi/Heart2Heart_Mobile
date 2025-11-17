package com.example.heart2heart.EmergencyBroadcast.presentation

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heart2heart.ECGExtraction.data.DetectionType
import com.example.heart2heart.EmergencyBroadcast.data.repository.ReportRepository
import com.example.heart2heart.EmergencyBroadcast.domain.EmergencyBroadcastService
import com.example.heart2heart.EmergencyBroadcast.domain.data.EmergencyEvent
import com.example.heart2heart.EmergencyBroadcast.presentation.state.EmergencyReportState
import com.example.heart2heart.R
import com.example.heart2heart.common.domain.model.ApiError
import com.example.heart2heart.home.domain.LiveLocationService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class EmergencyBroadcastViewModel @Inject constructor(
    private val emergencyBroadcastService: EmergencyBroadcastService,
    private val reportRepository: ReportRepository,
    private val liveLocationService: LiveLocationService,
    @ApplicationContext private val context: Context,
): ViewModel() {

    private val TOTAL_SOS_SECOND_WARNING = 10_000L

    private val INTERVAL_VIBRATE = 500L

    private val _timeLeftSeconds = MutableStateFlow(0L)
    val timeLeftSeconds: StateFlow<Long> = _timeLeftSeconds

    private var timerJob: Job? = null

    private val _isRunningVibrate = MutableStateFlow(false)
    val isRunningVibrate: StateFlow<Boolean>
            get() = _isRunningVibrate.asStateFlow()

    private val _emergencyReportState = MutableStateFlow(EmergencyReportState(
        username = "None",
        isOpenDialog = false,
        type = null,
        isSOSObserverOpen = false,
    ))
    val emergencyReportState: StateFlow<EmergencyReportState>
        get() = _emergencyReportState.asStateFlow()


    init {
        startListener()
    }

    fun startWarningTimer() {
        if (_isRunningVibrate.value) {
            return
        }
        _isRunningVibrate.update { true }
        timerJob?.cancel()
        var remainingTimeMs = TOTAL_SOS_SECOND_WARNING
        _timeLeftSeconds.value = remainingTimeMs

        timerJob = viewModelScope.launch {
            emergencyBroadcastService.playSound(R.raw.warning_sound_2)
            while (remainingTimeMs > 0) {
                delay(INTERVAL_VIBRATE)
                remainingTimeMs -= INTERVAL_VIBRATE

                _timeLeftSeconds.value = remainingTimeMs

                emergencyBroadcastService.vibrate()
            }

            _isRunningVibrate.update { false }
            emergencyBroadcastService.emitSOSPassed()
            emergencyBroadcastService.stopAndReleaseFocus()
        }
    }

    fun cancelSOS() {
        emergencyBroadcastService.stopAndReleaseFocus()
        _isRunningVibrate.update { false }
        timerJob?.cancel()
    }

    fun startListener() {
        viewModelScope.launch(Dispatchers.IO) {
            emergencyBroadcastService.events.collect {
                message ->
                when (message) {
                    is EmergencyEvent.EmergencyReportAmbulatory -> {
                        _emergencyReportState.update {
                            it.copy(
                                isOpenDialog = true,
                                type = message.reportType
                            )
                        }
                    }
                    is EmergencyEvent.EmergencyReportObserver -> {
                        _emergencyReportState.update {
                            it.copy(
                                isOpenDialog = true,
                                type = message.reportType,
                                username = message.username,
                            )
                        }
                    }
                    is EmergencyEvent.EmergencySOSObserver -> {
                        _emergencyReportState.update {
                            it.copy(
                                isSOSObserverOpen = true,
                                username = message.username,
                            )
                        }
                    }
                    EmergencyEvent.EmergencySent -> {
                        reportRepository.sendSOS(
                            LocalDateTime.now(),
                            lat = liveLocationService.locationState.value.longitude.toFloat(),
                            longitude = liveLocationService.locationState.value.latitude.toFloat()
                        ).fold(
                            ifLeft = {
                                networkError ->
                                val errorMessage = when (networkError.error) {
                                    ApiError.NetworkError -> "No internet connection. Please try again."
                                    ApiError.Unauthorized -> "Invalid email or password."
                                    ApiError.BadRequest -> "There was an issue with your request."
                                    ApiError.NotFound -> "User not found."
                                    ApiError.ServerError -> "Server error. Please try again later."
                                    else -> "An unknown error occurred."
                                }
                            },
                            ifRight = {
                                it ->
                            }
                        )
                    }
                }
            }
        }
    }

    fun confirmReport() {
        _emergencyReportState.update {
            it.copy(
                isOpenDialog = false,
                type = null,
                username = null,
            )
        }
    }

    fun confirmSOSAmbulatory() {
        _emergencyReportState.update {
            it.copy(
                isSOSObserverOpen = false,
                username = null,
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}