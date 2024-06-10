package com.esosa.f5pi_backend.controllers.responses

import com.esosa.f5pi_backend.data.models.PlayerStatistics
import java.util.UUID

data class PlayerResponse(
    val playerId: UUID,
    val name: String,
    val statistics: PlayerStatistics
)