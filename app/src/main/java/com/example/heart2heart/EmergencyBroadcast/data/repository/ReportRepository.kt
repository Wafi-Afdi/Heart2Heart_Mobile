package com.example.heart2heart.EmergencyBroadcast.data.repository

import android.content.Context
import arrow.core.Either
import arrow.core.raise.either
import com.example.heart2heart.ECGExtraction.data.ReportType
import com.example.heart2heart.EmergencyBroadcast.data.dto.ArrhythmiaDetailReport
import com.example.heart2heart.EmergencyBroadcast.data.dto.ArrhythmiaReportListsDTO
import com.example.heart2heart.EmergencyBroadcast.data.dto.GenerateDiagnosisDTO
import com.example.heart2heart.EmergencyBroadcast.data.dto.GenerateDiagnosisTypeDTO
import com.example.heart2heart.EmergencyBroadcast.data.dto.ReportDTO
import com.example.heart2heart.EmergencyBroadcast.data.dto.ReportSosDto
import com.example.heart2heart.EmergencyBroadcast.data.remote.ReportAPI
import com.example.heart2heart.common.data.toGeneralError
import com.example.heart2heart.common.domain.model.NetworkError
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject


class ReportRepository @Inject constructor(
    private val reportAPI: ReportAPI
) {
    suspend fun getAllReport(userId: UUID): Either<NetworkError, ArrhythmiaReportListsDTO> {
        return Either.catch {
            reportAPI.getAllReport(userId)
        }.mapLeft {
            it.toGeneralError()
        }
    }

    suspend fun getReportById(reportId: UUID): Either<NetworkError, ArrhythmiaDetailReport> {
        return Either.catch {
            reportAPI.getReport(reportId)
        }.mapLeft {
            it.toGeneralError()
        }
    }

    suspend fun sendSOS(ts: LocalDateTime, lat: Float, longitude: Float): Either<NetworkError, String> {
        return Either.catch {
            reportAPI.postSOS(reportSosDto = ReportSosDto(ts = ts.toString(), lat = lat, longitude = longitude))
        }.mapLeft {
            it.toGeneralError()
        }
    }

    suspend fun sendTachyOrBradyOrAsys(type: String, ts: LocalDateTime): Either<NetworkError, String> {
        return Either.catch {
            reportAPI.generateTachyOrBrady(GenerateDiagnosisTypeDTO(ts.toString(), type))
        }.mapLeft {
            it.toGeneralError()
        }
    }

    suspend fun generateDiagnosis(ts: LocalDateTime): Either<NetworkError, String> {
        return Either.catch {
            reportAPI.generateDiagnosis(GenerateDiagnosisDTO(ts.toString()))
        }.mapLeft {
            it.toGeneralError()
        }
    }

    fun translateToReportTypeFromString(input :String): ReportType {
        return when (input) {
            "SOS" -> ReportType.SOS
            "Atrial Fibrillation" -> ReportType.AFIB
            "Ventricular Tachycardia" -> ReportType.VT
            "Ventricular Fibrillation" -> ReportType.VFIB
            "Bradycardia" -> ReportType.BRADYCARDHIA
            "Tachycardia" -> ReportType.TACHYCARDHIA
            "Asystole" -> ReportType.ASYSTOLE
            "Normal Rhythm" -> ReportType.NORMAL
            else -> ReportType.UNKNOWN
        }
    }
}