package com.esosa.f5pi_backend.controllers.requests

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class TeamRequest(
    @field:NotNull(message = "Members list cannot be null")
    @field:Size(min = 5, max = 11, message = "Members list size must be between 5 and 11")
    val members: List<MemberRequest>,
)