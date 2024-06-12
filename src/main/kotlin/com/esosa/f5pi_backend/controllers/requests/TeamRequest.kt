package com.esosa.f5pi_backend.controllers.requests

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class TeamRequest(
    @field:NotNull(message = "Winner value must not be null")
    val winner: Boolean,

    @field:NotNull(message = "Members list cannot be null")
    @field:Size(min = 5, max = 5, message = "Members list size must be 2")
    val members: List<MemberRequest>,
)