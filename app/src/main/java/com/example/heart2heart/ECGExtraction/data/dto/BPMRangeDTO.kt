package com.example.heart2heart.ECGExtraction.data.dto

import java.util.UUID

data class BPMRangeDTO(
    val userId: UUID,
    val bpmDatas: List<BpmDataDTO>,
)
