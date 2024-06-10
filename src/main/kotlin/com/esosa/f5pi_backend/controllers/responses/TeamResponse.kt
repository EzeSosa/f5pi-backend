package com.esosa.f5pi_backend.controllers.responses

data class TeamResponse(
    val members: List<MemberResponse>,
    val winner: Boolean
)