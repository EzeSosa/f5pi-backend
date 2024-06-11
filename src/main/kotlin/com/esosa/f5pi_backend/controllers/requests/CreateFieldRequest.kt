package com.esosa.f5pi_backend.controllers.requests

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.util.UUID

data class CreateFieldRequest(
    @field:NotBlank(message = "Field name must not be empty")
    @field:Size(max = 20, message = "Field name must be less than 20 characters")
    val name: String,

    @field:NotNull(message = "User id must not be null")
    val userId: UUID
)