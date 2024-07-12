package com.esosa.f5pi_backend.controllers.requests

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.util.UUID

data class GenerateTeamsRequest(
    @field:NotNull(message = "Players list cannot be null")
    @field:Size(min = 10, max = 10, message = "Players list size must be 10")
    val playersId: List<UUID>
)