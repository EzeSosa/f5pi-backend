package com.esosa.f5pi_backend.controllers.requests

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdatePlayerRequest(
    @field:NotBlank(message = "Player name must not be empty")
    @field:Size(max = 25, message = "Player name must be less than 25 characters")
    val name: String
)