package com.esosa.f5pi_backend.controllers.implementations

import com.esosa.f5pi_backend.controllers.interfaces.IAIController
import com.esosa.f5pi_backend.controllers.requests.GenerateTeamsRequest
import com.esosa.f5pi_backend.controllers.responses.GenerateTeamsResponse
import com.esosa.f5pi_backend.services.interfaces.IAIService
import org.springframework.web.bind.annotation.RestController

@RestController
class AIController(private val aiService: IAIService) : IAIController {
    override fun generateTeams(generateTeamsRequest: GenerateTeamsRequest): GenerateTeamsResponse =
        aiService.generateTeams(generateTeamsRequest)
}