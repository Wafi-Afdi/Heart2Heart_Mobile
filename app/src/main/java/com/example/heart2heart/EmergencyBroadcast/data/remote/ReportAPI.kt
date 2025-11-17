package com.example.heart2heart.EmergencyBroadcast.data.remote

import com.example.heart2heart.BuildConfig
import com.example.heart2heart.EmergencyBroadcast.data.dto.ArrhythmiaDetailReport
import com.example.heart2heart.EmergencyBroadcast.data.dto.ArrhythmiaReportListsDTO
import com.example.heart2heart.EmergencyBroadcast.data.dto.GenerateDiagnosisDTO
import com.example.heart2heart.EmergencyBroadcast.data.dto.GenerateDiagnosisTypeDTO
import com.example.heart2heart.EmergencyBroadcast.data.dto.ReportDTO
import com.example.heart2heart.EmergencyBroadcast.data.dto.ReportSosDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.time.LocalDateTime
import java.util.UUID

interface ReportAPI {

    @POST("/api/report/SOS")
    suspend fun postSOS(
        @Body reportSosDto: ReportSosDto
    ): String

    @POST("/api/report/generateDiagnosis")
    suspend fun generateDiagnosis(
        @Body generateDiagnosisDTO: GenerateDiagnosisDTO
    ): String

    @POST("/api/report/generateTachyOrBrady")
    suspend fun generateTachyOrBrady(
        @Body generateDiagnosisTypeDTO: GenerateDiagnosisTypeDTO
    ): String

    @GET("/api/report/")
    suspend fun getReport(
        @Query("reportId") reportId: UUID,
    ): ArrhythmiaDetailReport

    @GET("/api/report/all")
    suspend fun getAllReport(
        @Query("userId") userId: UUID,
    ): ArrhythmiaReportListsDTO

    companion object {
        const val BASE_URL = BuildConfig.BE_API_LOCAL
    }
}