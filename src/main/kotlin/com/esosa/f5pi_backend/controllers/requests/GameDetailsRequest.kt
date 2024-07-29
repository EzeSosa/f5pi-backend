package com.esosa.f5pi_backend.controllers.requests

import jakarta.validation.constraints.Size
import jakarta.validation.constraints.NotNull

data class GameDetailsRequest(
    @field:NotNull(message = "Teams list cannot be null")
    @field:Size(min = 2, max = 2, message = "Teams list size must be 2")
    val teams: List<TeamRequest>
)