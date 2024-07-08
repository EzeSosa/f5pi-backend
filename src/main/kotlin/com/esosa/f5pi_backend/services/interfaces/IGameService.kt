package com.esosa.f5pi_backend.services.interfaces

import com.esosa.f5pi_backend.controllers.requests.CreateGameRequest
import com.esosa.f5pi_backend.controllers.requests.GameDetailsRequest
import com.esosa.f5pi_backend.controllers.requests.UpdateGameRequest
import com.esosa.f5pi_backend.controllers.responses.GameDetailsResponse
import com.esosa.f5pi_backend.controllers.responses.GameResponse
import com.esosa.f5pi_backend.data.models.Game
import com.esosa.f5pi_backend.data.models.User
import java.time.LocalDate
import java.util.UUID

interface IGameService {
    fun getGameDetails(gameId: UUID): GameDetailsResponse
    fun saveGame(createGameRequest: CreateGameRequest): GameResponse
    fun saveGameDetails(gameId: UUID, gameDetailsRequest: GameDetailsRequest)
    fun updateGame(gameId: UUID, updateGameRequest: UpdateGameRequest): GameResponse
    fun deleteGame(gameId: UUID)
    fun findGameByIdOrThrowException(gameId: UUID): Game
    fun getGamesByUser(
        user: User,
        dateFrom: LocalDate? = null,
        dateTo: LocalDate? = null,
        official: Boolean? = null
    ): List<GameResponse>
}