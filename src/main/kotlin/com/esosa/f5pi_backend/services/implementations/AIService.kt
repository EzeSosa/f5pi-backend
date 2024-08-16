package com.esosa.f5pi_backend.services.implementations

import com.esosa.f5pi_backend.ai.AIClient
import com.esosa.f5pi_backend.controllers.requests.GenerateTeamsRequest
import com.esosa.f5pi_backend.controllers.responses.GenerateTeamsResponse
import com.esosa.f5pi_backend.controllers.responses.PlayerResponse
import com.esosa.f5pi_backend.controllers.responses.PlayerStatisticsResponse
import com.esosa.f5pi_backend.data.models.Player
import com.esosa.f5pi_backend.ai.requests.QueryRequest
import com.esosa.f5pi_backend.ai.responses.QueryResponse
import com.esosa.f5pi_backend.ai.responses.QueryTeamsResponse
import com.esosa.f5pi_backend.services.interfaces.IAIService
import com.esosa.f5pi_backend.services.interfaces.IPlayerService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class AIService(
    private val playerService: IPlayerService,
    private val aiClient: AIClient
) : IAIService {

    @Value("\${ai.message}")
    lateinit var BASE_MESSAGE: String

    override fun generateTeams(generateTeamsRequest: GenerateTeamsRequest): GenerateTeamsResponse {
        val playerByName = mutableMapOf<String, Player>()
        val requestMessage = buildRequestMessage(generateTeamsRequest, playerByName)
        val response = aiClient.generateTeams(requestMessage)
        return response.buildGenerateTeamsResponse(playerByName)
    }

    private fun buildRequestMessage(generateTeamsRequest: GenerateTeamsRequest, playerByName: MutableMap<String, Player>): QueryRequest =
        QueryRequest(
            buildString {
                append(BASE_MESSAGE.trimIndent())
                generateTeamsRequest.playersId.forEach { playerId ->
                    val player = playerService.findPlayerByIdOrThrowException(playerId)
                    val playerStatistics = playerService.getPlayerStatistics(playerId)
                    playerByName[player.name] = player
                    append(playerStatistics.listStatistics(player.name))
                }
            }
        )

    private fun PlayerStatisticsResponse.listStatistics(playerName: String): String = """
        | Name: $playerName, Total matches: $games, 
        | Wins: $wins, Draws: $draws, Losses: $losses,
        | Goals scored: $goals, Own goals: $ownGoals
        | -
    """.trimMargin()

    private fun QueryResponse.buildGenerateTeamsResponse(playerByName: Map<String, Player>) =
        GenerateTeamsResponse(explanation, teams.buildTeams(playerByName))

    private fun QueryTeamsResponse.buildTeams(playerByName: Map<String, Player>): List<List<PlayerResponse>> =
        listOf(
            team1.map { playerByName[it]!!.buildPlayerResponse() },
            team2.map { playerByName[it]!!.buildPlayerResponse() }
        )
}