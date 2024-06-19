package com.esosa.f5pi_backend.services.interfaces

import com.esosa.f5pi_backend.controllers.requests.TeamRequest
import com.esosa.f5pi_backend.data.models.GameDetails

interface ITeamService {
    fun saveTeam(gameDetails: GameDetails, teamRequest: TeamRequest, official: Boolean, price: Double)
}