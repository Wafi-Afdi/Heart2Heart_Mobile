package com.example.heart2heart.ECGExtraction.data.dto

import java.util.UUID

data class publishEcgDTO(
    val userId: UUID?,
    val ecgData: List<EcgDataDTO> = emptyList()
)