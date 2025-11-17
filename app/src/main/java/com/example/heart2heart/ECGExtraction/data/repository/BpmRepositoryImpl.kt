package com.example.heart2heart.ECGExtraction.data.repository

import arrow.core.Either
import com.example.heart2heart.ECGExtraction.data.dto.BPMRangeDTO
import com.example.heart2heart.ECGExtraction.data.dto.BpmDataDTO
import com.example.heart2heart.ECGExtraction.data.dto.BpmPublishDTO
import com.example.heart2heart.ECGExtraction.data.remote.BpmDataAPI
import com.example.heart2heart.auth.repository.ProfileRepository
import com.example.heart2heart.common.data.toGeneralError
import com.example.heart2heart.common.domain.model.NetworkError
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

class BpmRepositoryImpl @Inject
constructor(
    private val bpmDataAPI: BpmDataAPI,
){
    suspend fun sendBPMData(bpmData: List<BpmDataDTO>, userId: UUID): Either<NetworkError, String> {
        return Either.catch {
            bpmDataAPI.publishSignal(BpmPublishDTO(
                userId = userId,
                bpmDatas = bpmData
            ))
        }.mapLeft {
            it.toGeneralError()
        }
    }

    suspend fun getBpmDataRange(start: LocalDateTime,
                                end: LocalDateTime,
                                userId: UUID): Either<NetworkError, BPMRangeDTO> {
        return Either.catch {
            bpmDataAPI.getRangeSignal(start.toString(), end.toString(), userId)
        }.mapLeft {
            it.toGeneralError()
        }
    }
}