package com.esosa.f5pi_backend.controllers.requests

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class UpdateUserRequest(
    @field:NotBlank(message = "Name must not be empty")
    val fullName: String,

    @field:Email(message = "Invalid email format")
    val email: String
)