package com.esosa.f5pi_backend.controllers.responses

data class LoginResponse(
    val user: UserResponse,
    val accessToken: String,
    val refreshToken: String
)