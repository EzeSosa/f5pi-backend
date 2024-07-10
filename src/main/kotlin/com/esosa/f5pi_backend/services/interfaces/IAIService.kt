package com.esosa.f5pi_backend.services.interfaces

import com.esosa.f5pi_backend.controllers.requests.GenerateTeamsRequest

interface IAIService {
    fun generateTeams(generateTeamsRequest: GenerateTeamsRequest): String
}