package com.esosa.f5pi_backend.services.implementations

import com.esosa.f5pi_backend.controllers.requests.GenerateTeamsRequest
import com.esosa.f5pi_backend.data.models.Player
import com.esosa.f5pi_backend.http.CustomHttpClient
import com.esosa.f5pi_backend.services.interfaces.IAIService
import com.esosa.f5pi_backend.services.interfaces.IPlayerService
import org.springframework.stereotype.Service

@Service
class AIService(
    private val playerService: IPlayerService,
    private val httpClient: CustomHttpClient
) : IAIService {

    private val baseMessage = """
        Generate two balanced teams with 5 players based on their matches played, their wins and their 
        goals scored. The response will only consists on the team number (1, 2) and the player names. 
        Here is the list of the players and their stats:
    """.trimIndent()
    private val url = "http://127.0.0.1:5000/generate-teams"

    override fun generateTeams(generateTeamsRequest: GenerateTeamsRequest): String {
        val requestMessage = buildString {
            append(baseMessage)
            generateTeamsRequest.playersId.forEach { playerId ->
                append(playerService.findPlayerByIdOrThrowException(playerId).listStatistics())
            }
        }
        return httpClient.doRequest(url, requestMessage)
    }

    private fun Player.listStatistics(): String = """
        |Name: $name, Total matches: ${playerStatistics.allGames}, 
        |Wins: ${playerStatistics.allWins}, Goals scored: ${playerStatistics.allGoals}
    """.trimMargin()
}