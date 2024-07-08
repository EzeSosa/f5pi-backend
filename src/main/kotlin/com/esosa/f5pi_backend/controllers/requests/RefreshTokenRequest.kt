package com.esosa.f5pi_backend.controllers.requests

import jakarta.validation.constraints.NotBlank

data class RefreshTokenRequest(
    @field:NotBlank(message = "Refresh token must not be empty")
    val refreshToken: String
)