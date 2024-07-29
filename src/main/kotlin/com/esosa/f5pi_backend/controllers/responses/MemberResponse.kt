package com.esosa.f5pi_backend.controllers.responses

data class MemberResponse(
    val playerName: String,
    val goalsScored: Int,
    val ownGoals: Int
)