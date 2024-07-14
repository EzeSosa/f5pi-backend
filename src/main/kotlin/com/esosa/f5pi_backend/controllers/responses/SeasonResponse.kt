package com.esosa.f5pi_backend.controllers.responses

import java.time.LocalDate

data class SeasonResponse(
    val name: String,
    val initialDate: LocalDate,
    val finalDate: LocalDate
)