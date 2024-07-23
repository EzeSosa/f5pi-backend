package com.esosa.f5pi_backend.controllers.responses

import java.util.UUID

data class UserResponse(
    val id: UUID,
    val username: String,
    val fullName: String,
    val email: String,
    val role: String
)