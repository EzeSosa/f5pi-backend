package com.esosa.f5pi_backend.controllers.responses

import java.util.UUID

data class LoginResponse(
    val userId: UUID,
    val accessToken: String
)