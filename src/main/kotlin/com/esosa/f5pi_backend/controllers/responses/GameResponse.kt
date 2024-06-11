package com.esosa.f5pi_backend.controllers.responses

import java.time.LocalDate
import java.util.UUID

data class GameResponse(
    val gameId: UUID,
    val date: LocalDate,
    val official: Boolean,
    val individualPrice: Double,
    val fieldName: String
)