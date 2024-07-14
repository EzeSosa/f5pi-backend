package com.esosa.f5pi_backend.controllers.requests

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDate

data class UpdateSeasonRequest(
    @field:NotBlank(message = "Season name must not be empty")
    @field:Size(max = 25, message = "Season name must be less than 20 characters")
    val name: String,

    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    val initialDate: LocalDate,

    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    val finalDate: LocalDate,
)