package com.esosa.f5pi_backend.controllers.requests

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class LoginRequest(
    @field:NotBlank(message = "Username must not be empty")
    @field:Size(max = 30, message = "Username must be less than 20 characters")
    val username: String,

    @field:NotBlank(message = "Password must not be empty")
    val password: String
)