package com.esosa.f5pi_backend.ai.responses

data class QueryResponse (
    val explanation: String,
    val teams: QueryTeamsResponse
)

data class QueryTeamsResponse (
    val team1: List<String>,
    val team2: List<String>
)