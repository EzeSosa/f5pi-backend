package com.esosa.f5pi_backend.controllers.requests

import jakarta.validation.constraints.NotBlank

data class CheckTokenRequest(
    @field:NotBlank(message = "Access token must not be empty")
    val accessToken: String
)