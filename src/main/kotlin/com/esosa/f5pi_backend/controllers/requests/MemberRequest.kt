package com.esosa.f5pi_backend.controllers.requests

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.util.UUID

data class MemberRequest(
    @field:NotNull(message = "Player ID must not be null")
    val playerId: UUID,

    @field:Min(value = 0, message = "Goals scored must be 0 or positive")
    val goalsScored: Int,

    @field:Min(value = 0, message = "Own goals scored must be 0 or positive")
    val ownGoals: Int
)