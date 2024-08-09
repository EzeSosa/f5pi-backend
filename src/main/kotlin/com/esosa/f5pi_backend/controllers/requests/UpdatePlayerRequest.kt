package com.esosa.f5pi_backend.controllers.requests

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.web.multipart.MultipartFile

data class UpdatePlayerRequest(
    @field:NotBlank(message = "Player name must not be empty")
    @field:Size(max = 25, message = "Player name must be less than 25 characters")
    val name: String,

    val image: MultipartFile?
)