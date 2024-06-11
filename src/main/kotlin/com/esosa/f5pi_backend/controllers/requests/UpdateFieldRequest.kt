package com.esosa.f5pi_backend.controllers.requests

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateFieldRequest(
    @field:NotBlank(message = "Field name must not be empty")
    @field:Size(max = 20, message = "Field name must be less than 20 characters")
    val name: String
)