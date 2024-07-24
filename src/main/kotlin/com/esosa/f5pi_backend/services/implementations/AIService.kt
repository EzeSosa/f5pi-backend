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
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service

@Service
class AIService(
    private val playerService: IPlayerService,
    private val httpClient: CustomHttpClient
) : IAIService {

    @Value("\${ai.message}")
    lateinit var BASE_MESSAGE: String

    @Value("\${ai.url}")
    lateinit var URL: String

    override fun generateTeams(generateTeamsRequest: GenerateTeamsRequest): GenerateTeamsResponse {
        val playerByName = mutableMapOf<String, Player>()
        val requestMessage = buildRequestMessage(generateTeamsRequest, playerByName)
        val jsonResponse = httpClient.doRequest(URL, QueryRequest(requestMessage), HttpMethod.POST)
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