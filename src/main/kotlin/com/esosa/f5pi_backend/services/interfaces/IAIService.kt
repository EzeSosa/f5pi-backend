package com.esosa.f5pi_backend.services.interfaces

import com.esosa.f5pi_backend.controllers.requests.GenerateTeamsRequest
import com.esosa.f5pi_backend.controllers.responses.GenerateTeamsResponse

interface IAIService {
    fun generateTeams(generateTeamsRequest: GenerateTeamsRequest): GenerateTeamsResponse
}