package com.esosa.f5pi_backend.services.interfaces

import com.esosa.f5pi_backend.controllers.requests.TeamRequest
import com.esosa.f5pi_backend.controllers.responses.TeamResponse
import com.esosa.f5pi_backend.data.enums.TeamResult

interface ITeamService {
    fun saveTeam(teamResult: TeamResult, teamGoals: Int, teamRequest: TeamRequest): TeamResponse
}