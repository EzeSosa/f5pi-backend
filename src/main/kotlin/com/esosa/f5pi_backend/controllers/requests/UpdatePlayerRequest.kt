package com.esosa.f5pi_backend.controllers.requests

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class UpdatePlayerRequest(
    @field:NotBlank(message = "Player name must not be empty")
    @field:Size(max = 25, message = "Player name must be less than 25 characters")
    val name: String,

    @field:Pattern(
        regexp = "^(http://www\\.|https://www\\.|http://|https://)?[a-z0-9]+([\\-.]?[a-z0-9]+)*(\\.[a-z]{2,5})(:[0-9]{1,5})?(/.*)?\$",
        message = "Invalid image URL"
    )
    val imageURL: String?
)