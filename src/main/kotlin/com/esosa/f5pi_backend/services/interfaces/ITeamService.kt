package com.esosa.f5pi_backend.services.interfaces

import com.esosa.f5pi_backend.controllers.requests.TeamRequest
import com.esosa.f5pi_backend.data.models.Game

interface ITeamService {
    fun saveTeam(game: Game, teamRequest: TeamRequest, official: Boolean, price: Double)
}