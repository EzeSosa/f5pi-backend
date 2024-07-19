package com.esosa.f5pi_backend.controllers.responses

import java.time.LocalDate
import java.util.UUID

data class SeasonResponse(
    val id: UUID,
    val name: String,
    val initialDate: LocalDate,
    val finalDate: LocalDate
)