package com.esosa.f5pi_backend.services.interfaces

import com.esosa.f5pi_backend.controllers.requests.TeamRequest
import com.esosa.f5pi_backend.data.enums.TeamResult
import com.esosa.f5pi_backend.data.models.GameDetails

interface ITeamService {
    fun saveTeam(gameDetails: GameDetails, teamResult: TeamResult, teamGoals: Int, teamRequest: TeamRequest)
}