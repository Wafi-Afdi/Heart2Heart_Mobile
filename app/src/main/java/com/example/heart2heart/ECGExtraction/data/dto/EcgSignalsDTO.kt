package com.example.heart2heart.ECGExtraction.data.dto

import java.util.UUID

data class EcgSignalsDTO(
    val userId: UUID,
    val ecgData: List<EcgDataDTO>
)