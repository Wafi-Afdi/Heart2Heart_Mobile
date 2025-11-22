package com.example.heart2heart.EmergencyBroadcast.presentation

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heart2heart.ECGExtraction.data.dto.EcgDataDTO
import com.example.heart2heart.EmergencyBroadcast.data.remote.ReportAPI
import com.example.heart2heart.EmergencyBroadcast.data.repository.ReportRepository
import com.example.heart2heart.EmergencyBroadcast.presentation.state.ReportUIState
import com.example.heart2heart.auth.repository.ProfileRepository
import com.example.heart2heart.common.domain.model.ApiError
import com.example.heart2heart.report.domain.model.ECGSegment
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.UUID
import javax.inject.Inject
import kotlin.collections.forEach

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

    suspend fun saveReport() {

    }

    fun onDownloadPressed() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (_reportModel.value.ecgSegment != null && _reportModel.value.ecgSegment!!.size != 0) {

                    val csvContent = createCsvString(_reportModel.value.ecgSegment!!)

                    saveToDownloads(
                        context = context,
                        csvContent = csvContent,
                        userId = _reportModel.value.username.toString(),
                        start = _reportModel.value.timestamp,
                        end = _reportModel.value.timestamp.plus(3 * _reportModel.value.ecgSegment!!.size.toLong(), ChronoUnit.MILLIS),
                        result = _reportModel.value.reportType.toString()
                    )
                    Toast.makeText(context, "ECG Segment Saved to Download", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "No ECG Segment", Toast.LENGTH_SHORT).show()
                }


            }
        }
    }

    suspend fun saveECGSegment() {

    }

    private fun createCsvString(data: List<ECGSegment>): String {
        val stringBuilder = StringBuilder()

        // 1. Add Header Row
        stringBuilder.appendLine("signal,ts")

        // 2. Add Data Rows
        data.forEach { dto ->
            // Using quotes around the timestamp is good practice in case it contains commas
            stringBuilder.appendLine("${dto.signal},${dto.timestamp.toString()}")
        }

        return stringBuilder.toString()
    }

    private fun saveToDownloads(
        context: Context,
        csvContent: String,
        userId: String,
        start: LocalDateTime,
        end: LocalDateTime,
        result: String,
    ) {
        // 1. Define file details
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm")
        val startStr = start.format(formatter)
        val endStr = end.format(formatter)
        val fileName = "report_ecg_data_${userId}_${startStr}_to_${endStr}_result_${result}.csv"

        val resolver = context.contentResolver

        // 2. Set file metadata
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        var uri: Uri? = null

        try {
            uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
                ?: throw IOException("Failed to create new MediaStore entry.")

            resolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(csvContent.toByteArray())
            }
        } catch (e: Exception) {
            uri?.let {
                resolver.delete(it, null, null)
            }
            throw IOException("Failed to save file: ${e.message}", e)
        }
    }

}