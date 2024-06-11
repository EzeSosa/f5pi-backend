package com.esosa.f5pi_backend.controllers.requests

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.time.LocalDate
import java.util.UUID

data class CreateGameRequest(
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    val date: LocalDate,

    @field:NotNull(message = "Official value must not be null")
    val official: Boolean,

    @field:Min(value = 0, message = "Price must not be negative (that would be great!)")
    val individualPrice: Double,

    @field:NotNull(message = "Field id must not be null")
    val fieldId: UUID,

    @field:NotNull(message = "User id must not be null")
    val userId: UUID
)