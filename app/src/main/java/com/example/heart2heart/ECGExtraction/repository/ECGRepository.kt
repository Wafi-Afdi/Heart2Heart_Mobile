package com.example.heart2heart.ECGExtraction.repository

import arrow.core.Either
import com.example.heart2heart.ECGExtraction.data.dto.EcgDataDTO
import com.example.heart2heart.ECGExtraction.data.dto.EcgSignalsDTO
import com.example.heart2heart.ECGExtraction.data.dto.publishEcgDTO
import com.example.heart2heart.ECGExtraction.model.ECGSignalDataModal
import com.example.heart2heart.ECGExtraction.model.ECGSignalDataSTM
import com.example.heart2heart.common.domain.model.NetworkError
import com.example.heart2heart.home.data.LocationData
import java.time.LocalDateTime
import java.util.UUID

interface ECGRepository {
    suspend fun saveBatch(data: List<ECGSignalDataModal>)

    suspend fun saveBatchToDb(dataDTO: publishEcgDTO): Either<NetworkError, String>

    suspend fun getDataRange(start: LocalDateTime, end: LocalDateTime, userId: UUID): Either<NetworkError, EcgSignalsDTO>
}