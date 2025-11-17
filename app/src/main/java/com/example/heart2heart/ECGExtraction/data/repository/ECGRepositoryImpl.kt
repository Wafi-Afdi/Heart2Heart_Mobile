package com.example.heart2heart.ECGExtraction.data.repository

import android.util.Log
import arrow.core.Either
import com.example.heart2heart.ECGExtraction.data.dao.EcgSignalDao
import com.example.heart2heart.ECGExtraction.data.dto.EcgSignalsDTO
import com.example.heart2heart.ECGExtraction.data.dto.publishEcgDTO
import com.example.heart2heart.ECGExtraction.data.remote.ECGExtractionAPI
import com.example.heart2heart.ECGExtraction.model.ECGSignalDataModal
import com.example.heart2heart.ECGExtraction.model.ECGSignalDataSTM
import com.example.heart2heart.ECGExtraction.repository.ECGRepository
import com.example.heart2heart.auth.repository.ProfileRepository
import com.example.heart2heart.common.data.toGeneralError
import com.example.heart2heart.common.domain.model.NetworkError
import jakarta.inject.Inject
import java.time.LocalDateTime
import java.util.UUID

class ECGRepositoryImpl @Inject constructor(
    private val ecgSignalDao: EcgSignalDao,
    private val ecgExtractionAPI: ECGExtractionAPI,
): ECGRepository {
    override suspend fun saveBatch(data: List<ECGSignalDataModal>) {
        try {
            ecgSignalDao.insertBatch(data)
            Log.i("ecgRepo", "Room: Successfully saved ${data.size} records.")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun saveBatchToDb(dataDTO: publishEcgDTO): Either<NetworkError, String> {
        return Either.catch {
            ecgExtractionAPI.publishSignal(dataDTO)
        }.mapLeft {
            it.toGeneralError()
        }
    }

    override suspend fun getDataRange(
        start: LocalDateTime,
        end: LocalDateTime,
        userId: UUID
    ): Either<NetworkError, EcgSignalsDTO> {
        return Either.catch {
            ecgExtractionAPI.getRangeSignal(start.toString(), end.toString(), userId)
        }.mapLeft {
            it.toGeneralError()
        }
    }
}