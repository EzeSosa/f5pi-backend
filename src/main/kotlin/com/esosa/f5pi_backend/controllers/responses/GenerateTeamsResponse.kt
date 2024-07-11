package com.esosa.f5pi_backend.controllers.responses

data class GenerateTeamsResponse(
    val explanation: String,
    val teams: List<List<String>>
)