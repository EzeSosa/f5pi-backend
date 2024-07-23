package com.esosa.f5pi_backend.controllers.requests

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class AuthRequest(
    @field:NotBlank(message = "Username must not be empty")
    @field:Size(max = 30, message = "Username must be less than 20 characters")
    val username: String,

    @field:NotBlank(message = "Password must not be empty")
    val password: String,

    @field:NotBlank(message = "Name must not be empty")
    val fullName: String,

    @field:Email(message = "Invalid email format")
    val email: String
)