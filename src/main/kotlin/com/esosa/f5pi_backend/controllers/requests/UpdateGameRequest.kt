package com.esosa.f5pi_backend.controllers.requests

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.Min
import java.time.LocalDate

data class UpdateGameRequest(
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    val date: LocalDate,

    @field:Min(value = 0, message = "Price must not be negative (that would be great!)")
    val individualPrice: Double
)