package com.esosa.f5pi_backend.controllers.implementations

import com.esosa.f5pi_backend.controllers.interfaces.IGameController
import com.esosa.f5pi_backend.controllers.requests.CreateGameRequest
import com.esosa.f5pi_backend.controllers.requests.GameDetailsRequest
import com.esosa.f5pi_backend.controllers.requests.UpdateGameRequest
import com.esosa.f5pi_backend.controllers.responses.GameDetailsResponse
import com.esosa.f5pi_backend.controllers.responses.GameResponse
import com.esosa.f5pi_backend.services.interfaces.IGameService
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class GameController(private val gameService: IGameService) : IGameController {
    override fun getGameDetails(gameId: UUID): GameDetailsResponse =
        gameService.getGameDetails(gameId)

    override fun saveGame(createGameRequest: CreateGameRequest): GameResponse =
        gameService.saveGame(createGameRequest)

    override fun saveGameDetails(gameId: UUID, gameDetailsRequest: GameDetailsRequest) =
        gameService.saveGameDetails(gameId, gameDetailsRequest)

    override fun updateGame(gameId: UUID, updateGameRequest: UpdateGameRequest): GameResponse =
        gameService.updateGame(gameId, updateGameRequest)

    override fun deleteGame(gameId: UUID) =
        gameService.deleteGame(gameId)
}