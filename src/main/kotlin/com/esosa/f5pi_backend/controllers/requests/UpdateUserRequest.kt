package com.esosa.f5pi_backend.controllers.requests

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateUserRequest(
    @field:NotBlank(message = "Name must not be empty")
    @field:Size(max = 30, message = "User name must be less than 30 characters")
    val fullName: String,

    @field:Email(message = "Invalid email format")
    val email: String
)