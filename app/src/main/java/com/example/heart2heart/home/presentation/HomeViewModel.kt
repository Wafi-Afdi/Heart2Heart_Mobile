package com.example.heart2heart.home.presentation

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heart2heart.ECGExtraction.data.ReportType
import com.example.heart2heart.ECGExtraction.model.ECGDataProcessingService
import com.example.heart2heart.EmergencyBroadcast.data.remote.ReportAPI
import com.example.heart2heart.EmergencyBroadcast.data.repository.ReportRepository
import com.example.heart2heart.auth.data.AppType
import com.example.heart2heart.auth.presentation.RegisterUIState
import com.example.heart2heart.auth.repository.AuthRepository
import com.example.heart2heart.auth.repository.ProfileRepository
import com.example.heart2heart.common.domain.model.ApiError
import com.example.heart2heart.home.data.LocationData
import com.example.heart2heart.home.domain.LiveLocationService
import com.example.heart2heart.home.presentation.state.UserHomeState
import com.example.heart2heart.report.domain.model.ReportData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val profileRepo: ProfileRepository,
    private val liveLocationService: LiveLocationService,
    private val ecgProcessingService: ECGDataProcessingService,
    private val reportRepository: ReportRepository
): ViewModel() {
    private val _userHomeState = MutableStateFlow(UserHomeState())
    private val _locationState = MutableStateFlow(LocationData())

    private val _isLoadingReport = MutableStateFlow(false)
    val isLoadingReport: StateFlow<Boolean>
        get() = _isLoadingReport.asStateFlow()

    val userHomeState = _userHomeState.asStateFlow()

    val heartBPM = ecgProcessingService.calculatedBPM

    val locationState = liveLocationService.locationState

    private val _listOfReports = MutableStateFlow<List<ReportData>>(emptyList())
    val listOfReports: StateFlow<List<ReportData>>
        get() = _listOfReports.asStateFlow()

    init {
        viewModelScope.launch {
            _userHomeState.update { currentState ->
                currentState.copy(
                    appType = profileRepo.appType.value,
                    name = profileRepo.userData.value.name
                )
            }

            combine(
                profileRepo.appType,
                profileRepo.userData
            ) { appType, userData ->
                _userHomeState.update { currentState ->
                    currentState.copy(
                        appType = appType,
                        name = userData.name
                    )
                }
            }.collect()
        }
    }

    fun fetchReportList() {
        val appType = profileRepo.appType.value
        var userId: String = ""
        var username: String = ""
        if (appType == AppType.AMBULATORY) {
            userId = profileRepo.userData.value.id
            username = profileRepo.userData.value.name
        } else {
            userId = profileRepo.ECGObserving.value.id
            username = profileRepo.ECGObserving.value.name
        }
        viewModelScope.launch {
            reportRepository.getAllReport(UUID.fromString(userId)).fold(
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
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                },
                ifRight = {
                    data ->
                    val result = data.arrhythmiaReports.map {
                            report ->
                        ReportData(
                            detectionType = stringToReportType(report.reportType),
                            reportId = UUID.fromString(report.reportId),
                            userId = UUID.fromString(userId),
                            username = username,
                            ts = LocalDateTime.parse(report.timestamp)
                        )
                    }
                    _listOfReports.value = result
                }
            )
        }
    }

    private fun stringToReportType(input: String): ReportType {
        if (input == "SOS") {
            return ReportType.SOS;
        } else if (input == "Atrial Fibrillation") {
            return ReportType.AFIB;
        } else if (input == "Ventricular Tachycardia") {
            return ReportType.VT;
        } else if (input == "Ventricular Fibrillation") {
            return ReportType.VFIB;
        } else if (input == "Bradycardia") {
            return ReportType.BRADYCARDHIA;
        } else if (input == "Tachycardia") {
            return ReportType.TACHYCARDHIA;
        } else if (input == "Asystole") {
            return ReportType.ASYSTOLE;
        } else if (input ==  "Normal Rhythm") {
            return ReportType.NORMAL;
        } else {
            return ReportType.UNKNOWN;
        }
    }

    fun reportTypeToStringTitle(reportType: ReportType): String {
        return when (reportType) {
            ReportType.SOS -> "SOS"
            ReportType.AFIB -> "Atrial Fibrillation"
            ReportType.VT -> "Ventricular Tachycardia"
            ReportType.VFIB -> "Ventricular Fibrillation"
            ReportType.BRADYCARDHIA -> "Bradycardia"
            ReportType.TACHYCARDHIA -> "Tachycardia"
            ReportType.ASYSTOLE -> "Asystole"
            ReportType.NORMAL -> "Normal Rhythm"
            ReportType.UNKNOWN -> "Is Being Processed"
        }
    }


    fun startTracking() {
        liveLocationService.startLocationUpdates()
    }

    fun stopTracking() {
        liveLocationService.stopLocationUpdates()
    }
}