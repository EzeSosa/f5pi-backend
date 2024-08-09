package com.esosa.f5pi_backend.controllers.requests

import jakarta.validation.constraints.Size
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

data class CreatePlayerRequest(
    @field:NotBlank(message = "Player name must not be empty")
    @field:Size(max = 25, message = "Player name must be less than 25 characters")
    val name: String,

    @field:NotNull(message = "User id must not be null")
    val userId: UUID,

    val image: MultipartFile?
)