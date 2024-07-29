package com.esosa.f5pi_backend.controllers.responses

import com.esosa.f5pi_backend.data.enums.TeamResult

data class TeamResponse(
    val result: TeamResult,
    val goals: Int,
    val members: List<MemberResponse>
)