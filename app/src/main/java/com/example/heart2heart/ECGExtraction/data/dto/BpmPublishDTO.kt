package com.example.heart2heart.ECGExtraction.data.dto

import java.util.UUID

data class BpmPublishDTO(
    val userId: UUID?,
    val bpmDatas: List<BpmDataDTO> = emptyList()
)
