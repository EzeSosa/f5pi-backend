package com.esosa.f5pi_backend.controllers.responses

import java.util.UUID

data class PlayerResponse(
    val playerId: UUID,
    val name: String,
    val statistics: PlayerStatisticsResponse
)