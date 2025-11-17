package com.example.heart2heart.ECGExtraction.data.remote

import com.example.heart2heart.BuildConfig
import com.example.heart2heart.ECGExtraction.data.dto.BPMRangeDTO
import com.example.heart2heart.ECGExtraction.data.dto.BpmPublishDTO
import com.example.heart2heart.ECGExtraction.data.dto.publishEcgDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.time.LocalDateTime
import java.util.UUID

interface BpmDataAPI {
    @POST("api/bpmData/publish")
    suspend fun publishSignal(@Body bpmPublishDTO: BpmPublishDTO): String

    @GET("api/bpmData/range")
    suspend fun getRangeSignal(
        @Query("start") start: String,
        @Query("end") end: String,
        @Query("userId") userId: UUID?
    ): BPMRangeDTO


    companion object {
        const val BASE_URL = BuildConfig.BE_API_LOCAL
    }
}