package com.esosa.f5pi_backend.services.implementations

import com.esosa.f5pi_backend.controllers.requests.GenerateTeamsRequest
import com.esosa.f5pi_backend.data.models.Player
import com.esosa.f5pi_backend.services.interfaces.IAIService
import com.esosa.f5pi_backend.services.interfaces.IPlayerService
import org.springframework.ai.chat.client.ChatClient
import org.springframework.stereotype.Service

@Service
class AIService(
    private val playerService: IPlayerService,
    private val chatClient: ChatClient.Builder
) : IAIService {

    private var message = """
        Generate two balanced teams with the names of 5 players based on their matches played, 
        their wins and their goals scored. Here is the list of the players and their stats:
    """.trimIndent()

    override fun generateTeams(generateTeamsRequest: GenerateTeamsRequest): String =
        with(generateTeamsRequest) {
            playersId.forEach { playerId ->
                message += playerService.findPlayerByIdOrThrowException(playerId).listStatistics()
            }
            queryAi(message)
        }

    private fun queryAi(message: String): String =
        chatClient
            .defaultSystem("You are a football analyst in charge to create football teams.")
            .build()
            .prompt()
            .user(message)
            .call()
            .content()

    private fun Player.listStatistics(): String =
        "\nName: ${name}, Total matches: ${playerStatistics.allGames}, " +
                "Wins: ${playerStatistics.allWins}, Goals scored: ${playerStatistics.allGoals}"
}