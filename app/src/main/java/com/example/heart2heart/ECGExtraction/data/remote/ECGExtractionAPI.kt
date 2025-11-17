package com.example.heart2heart.ECGExtraction.data.remote

import com.example.heart2heart.BuildConfig
import com.example.heart2heart.ECGExtraction.data.dto.EcgDataDTO
import com.example.heart2heart.ECGExtraction.data.dto.EcgSignalsDTO
import com.example.heart2heart.ECGExtraction.data.dto.publishEcgDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.time.LocalDateTime
import java.util.UUID

interface ECGExtractionAPI {

    @POST("api/ecgSignal/publish")
    suspend fun publishSignal(@Body publishEcgDTO: publishEcgDTO): String

    @GET("api/ecgSignal/range")
    suspend fun getRangeSignal(
        @Query("start") start: String,
        @Query("end") end: String,
        @Query("userId") userId: UUID?
    ): EcgSignalsDTO


    companion object {
        const val BASE_URL = BuildConfig.BE_API_LOCAL
    }
}