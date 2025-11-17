package com.example.heart2heart.report.data.repository

import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.example.heart2heart.ECGExtraction.data.ReportType
import com.example.heart2heart.ECGExtraction.data.dto.BpmDataDTO
import com.example.heart2heart.ECGExtraction.data.remote.ECGExtractionAPI
import com.example.heart2heart.ECGExtraction.data.repository.BpmRepositoryImpl
import com.example.heart2heart.ECGExtraction.repository.ECGRepository
import com.example.heart2heart.EmergencyBroadcast.data.repository.ReportRepository
import com.example.heart2heart.EmergencyBroadcast.domain.data.EmergencyEvent
import com.example.heart2heart.auth.data.AppType
import com.example.heart2heart.auth.repository.ProfileRepository
import com.example.heart2heart.common.domain.model.ApiError
import com.example.heart2heart.report.domain.model.AverageBPM
import com.example.heart2heart.report.domain.model.ReportData
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject

class StatisticRepository @Inject constructor(
    private val reportRepository: ReportRepository,
    private val profileRepo: ProfileRepository,
    private val ecgRepository: ECGRepository,
    private val bpmRepositoryImpl: BpmRepositoryImpl
) {


    private val _toastMessage  = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String>
        get() = _toastMessage.asSharedFlow()

    private val _listOfReports = MutableStateFlow<List<ReportData>>(emptyList())
    val listOfReports: StateFlow<List<ReportData>>
        get() = _listOfReports.asStateFlow()

    private val _listOfAverage = MutableStateFlow<List<AverageBPM>>(emptyList())
    val listOfAverage: StateFlow<List<AverageBPM>>
        get() = _listOfAverage.asStateFlow()

    suspend fun fetchReportList() {
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
                    _toastMessage.emit(errorMessage)
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

    suspend fun fetchBPMData(start: LocalDateTime, end: LocalDateTime) {
        var userId: UUID
        if (profileRepo.appType.value == AppType.AMBULATORY) {
            userId = UUID.fromString(profileRepo.userData.value.id)
        } else {
            userId = UUID.fromString(profileRepo.ECGObserving.value.id)
        }
        bpmRepositoryImpl.getBpmDataRange(start, end, userId).fold(
            ifLeft = {
                    networkError ->
                val errorMessage = when (networkError.error) {
                    ApiError.NetworkError -> "No internet connection. Please try again."
                    ApiError.Unauthorized -> "Invalid data."
                    ApiError.BadRequest -> "There was an issue with your request."
                    ApiError.NotFound -> "User not found."
                    ApiError.ServerError -> "Server error. Please try again later."
                    else -> "An unknown error occurred."
                }
                _toastMessage.emit(errorMessage)
            },
            ifRight = {
                res ->
                val medianBPM = calculateAverageBPMPerDay(res.bpmDatas)
                _listOfAverage.value = medianBPM
                _toastMessage.emit("Data fetched")
            }
        )
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

    private fun calculateAverageBPMPerDay(bpmRange :List<BpmDataDTO>): List<AverageBPM> {
        val parsedData = bpmRange.mapNotNull { dto ->
            try {
                val dateTime = LocalDateTime.parse(dto.ts)
                dateTime.toLocalDate() to dto
            } catch (e: Exception) {
                null
            }
        }

        val groupedByDate = parsedData.groupBy(
            keySelector = { (date, dto) -> date },
            valueTransform = { (date, dto) -> dto }
        )
        // 3. Calculate the median BPM for each group
        val resultList = groupedByDate.map { (date, dtos) ->
            // Extract all valid BPM values from the DTOs in the group
            val bpms = dtos.mapNotNull { it.bpm }

            // Calculate the median for the list of BPMs
            val medianBpm = calculateMedian(bpms)

            AverageBPM(
                bpm = medianBpm,
                timestamp = date.atStartOfDay()
            )
        }
        return resultList.sortedBy { it.timestamp }
    }

    private fun calculateMedian(bpms: List<Float>): Float {
        if (bpms.isEmpty()) return 0f

        // 1. Sort the list
        val sortedBpms = bpms.sorted()
        val size = sortedBpms.size

        val middleIndex = size / 2

        return if (size % 2 == 1) {
            sortedBpms[middleIndex]
        } else {
            val a = sortedBpms[middleIndex - 1]
            val b = sortedBpms[middleIndex]
            (a + b) / 2f
        }
    }
}