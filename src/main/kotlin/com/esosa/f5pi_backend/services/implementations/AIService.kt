package com.esosa.f5pi_backend.services.implementations

import com.esosa.f5pi_backend.controllers.requests.GenerateTeamsRequest
import com.esosa.f5pi_backend.controllers.responses.GenerateTeamsResponse
import com.esosa.f5pi_backend.controllers.responses.PlayerResponse
import com.esosa.f5pi_backend.controllers.responses.PlayerStatisticsResponse
import com.esosa.f5pi_backend.data.models.Player
import com.esosa.f5pi_backend.http.CustomHttpClient
import com.esosa.f5pi_backend.services.interfaces.IAIService
import com.esosa.f5pi_backend.services.interfaces.IPlayerService
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Service

@Service
class AIService(
    private val playerService: IPlayerService,
    private val httpClient: CustomHttpClient
) : IAIService {

    private val BASE_MESSAGE = """
        Generate two balanced teams with 5 players based on their matches played, their wins and their 
        goals scored. Balanced means the two teams have to have the same skill level, so the best players
        must be separated. Here is the list of the players and their stats:
        """
    private val URL = "http://127.0.0.1:5000/generate-teams"

    override fun generateTeams(generateTeamsRequest: GenerateTeamsRequest): GenerateTeamsResponse {
        val playerByName = mutableMapOf<String, Player>()
        val requestMessage = buildRequestMessage(generateTeamsRequest, playerByName)
        val jsonResponse = httpClient.doRequest(URL, QueryRequest(requestMessage))
        return jsonResponse.buildGenerateTeamsResponse(playerByName)
    }

    private fun buildRequestMessage(generateTeamsRequest: GenerateTeamsRequest, playerByName: MutableMap<String, Player>): String =
        buildString {
            append(BASE_MESSAGE.trimIndent())
            generateTeamsRequest.playersId.forEach { playerId ->
                val player = playerService.findPlayerByIdOrThrowException(playerId)
                val playerStatistics = playerService.getPlayerStatistics(playerId)
                playerByName[player.name] = player
                append(playerStatistics.listStatistics(player.name))
            }
        }

    private fun PlayerStatisticsResponse.listStatistics(playerName: String): String = """
        |Name: $playerName, Total matches: $games, 
        |Wins: $wins, Draws: $draws, Losses: $losses,
        |Goals scored: $goals
    """.trimMargin()

    private fun JsonNode.buildGenerateTeamsResponse(playerByName: Map<String, Player>): GenerateTeamsResponse {
        val explanation = this["explanation"].asText()
        val teams = this["teams"].buildTeamsList(playerByName)
        return GenerateTeamsResponse(explanation, teams)
    }

    private fun JsonNode.buildTeamsList(playerByName: Map<String, Player>): List<List<PlayerResponse>> =
        this.fields().asSequence()
            .map { it.value.toPlayerList(playerByName) }
            .toList()

    private fun JsonNode.toPlayerList(playerByName: Map<String, Player>): List<PlayerResponse> =
        this.map { playerByName[it.asText()]!!.buildPlayerResponse() }

    data class QueryRequest(val query: String)
}