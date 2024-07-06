package com.esosa.f5pi_backend.controllers.requests

import jakarta.validation.constraints.Size
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import java.util.UUID

data class CreatePlayerRequest(
    @field:NotBlank(message = "Player name must not be empty")
    @field:Size(max = 25, message = "Player name must be less than 25 characters")
    val name: String,

    @field:NotNull(message = "User id must not be null")
    val userId: UUID,

    @field:Pattern(
        regexp = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]?[a-z0-9]+)*(\\.[a-z]{2,5})(:[0-9]{1,5})?(\\/.*)?\$",
        message = "Invalid image URL"
    )
    val imageURL: String?
)