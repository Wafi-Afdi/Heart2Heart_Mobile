package com.example.heart2heart.EmergencyBroadcast.presentation

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heart2heart.EmergencyBroadcast.data.remote.ReportAPI
import com.example.heart2heart.EmergencyBroadcast.data.repository.ReportRepository
import com.example.heart2heart.EmergencyBroadcast.presentation.state.ReportUIState
import com.example.heart2heart.auth.repository.ProfileRepository
import com.example.heart2heart.common.domain.model.ApiError
import com.example.heart2heart.report.domain.model.ECGSegment
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val reportRepository: ReportRepository,
    private val profileRepository: ProfileRepository,
    @ApplicationContext private val context: Context,
): ViewModel() {
    private val _reportModel = MutableStateFlow(ReportUIState())
    val reportModel: StateFlow<ReportUIState>
        get() = _reportModel.asStateFlow()

    private val _isLoadingReport = MutableStateFlow(false)
    val isLoadingReport: StateFlow<Boolean>
        get() = _isLoadingReport.asStateFlow()

    fun fetchReport(reportId: UUID) {
        viewModelScope.launch {
            _isLoadingReport.update { true }
            reportRepository.getReportById(reportId).fold(
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
                    res ->
                        val start = LocalDateTime.parse(res.report.ecgSegment.start)
                        var counter = -3;
                        val convertedEcgSegment = res.report.ecgSegment?.signal?.map {
                            it ->
                            counter+=3
                            ECGSegment(it, start.plus(counter.toLong(), ChronoUnit.MILLIS))
                        }
                        if (!res.isEmpty) {
                            _reportModel.update {
                                it.copy(
                                    ecgSegment = convertedEcgSegment,
                                    reportId = UUID.fromString(res.report.reportId),
                                    timestamp = LocalDateTime.parse(res.report.timestamp),
                                    username = res.userData.name,
                                    reportType = reportRepository.translateToReportTypeFromString(res.report.reportType)
                                )
                            }
                        }

                }
            )
            _isLoadingReport.update { false }
        }
    }

    fun saveReport() {

    }

    fun saveECGSegment() {

    }

}