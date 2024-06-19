package com.esosa.f5pi_backend.services.implementations

import com.esosa.f5pi_backend.controllers.requests.CreateGameRequest
import com.esosa.f5pi_backend.controllers.requests.GameDetailsRequest
import com.esosa.f5pi_backend.controllers.requests.UpdateGameRequest
import com.esosa.f5pi_backend.controllers.responses.GameDetailsResponse
import com.esosa.f5pi_backend.controllers.responses.GameResponse
import com.esosa.f5pi_backend.data.models.Field
import com.esosa.f5pi_backend.data.models.Game
import com.esosa.f5pi_backend.data.models.User
import com.esosa.f5pi_backend.data.repositories.IGameRepository
import com.esosa.f5pi_backend.services.interfaces.IFieldService
import com.esosa.f5pi_backend.services.interfaces.IGameService
import com.esosa.f5pi_backend.services.interfaces.ITeamService
import com.esosa.f5pi_backend.services.interfaces.IUserService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.util.UUID

@Service
class GameService(
    private val gameRepository: IGameRepository,
    private val userService: IUserService,
    private val teamService: ITeamService,
    private val fieldService: IFieldService,
) : IGameService {

    override fun getGameDetails(gameId: UUID): GameDetailsResponse =
        findGameByIdOrThrowException(gameId)
            .details
            .buildGameDetailsResponse()

    override fun saveGame(createGameRequest: CreateGameRequest): GameResponse =
        with(createGameRequest) {
            val user = userService.findUserByIdOrThrowException(userId)
            val field = fieldService.findFieldByIdOrThrowException(fieldId)
            gameRepository.save(buildGame(field, user))
                .buildGameResponse()
        }

    override fun saveGameDetails(gameId: UUID, gameDetailsRequest: GameDetailsRequest) {
        findGameByIdOrThrowException(gameId).let { game ->
            gameDetailsRequest.teams
                .forEach { teamRequest -> teamService.saveTeam(game.details, teamRequest, game.official, game.individualPrice) }
        }
    }

    override fun updateGame(gameId: UUID, updateGameRequest: UpdateGameRequest): GameResponse =
        findGameByIdOrThrowException(gameId).let { game ->
            gameRepository.save(game.apply {
                date = updateGameRequest.date
                official = updateGameRequest.official
                individualPrice = updateGameRequest.individualPrice
            }).buildGameResponse()
        }

    override fun deleteGame(gameId: UUID) {
        ifGameDoesNotExistThrowException(gameId)
        gameRepository.deleteById(gameId)
    }

    override fun findGameByIdOrThrowException(gameId: UUID): Game =
        gameRepository.findById(gameId)
            .orElseThrow { ResponseStatusException(HttpStatus.BAD_REQUEST, "Game with id $gameId does not exist") }

    override fun getGamesByUser(user: User, dateFrom: LocalDate?, dateTo: LocalDate?): List<GameResponse> =
        gameRepository.findByUser(user, dateFrom, dateTo)
            .map(Game::buildGameResponse)

    private fun ifGameDoesNotExistThrowException(gameId: UUID) {
        if (!gameRepository.existsById(gameId))
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Game with id $gameId does not exist")
    }

    private fun CreateGameRequest.buildGame(field: Field, user: User) = Game(date, official, individualPrice, field, user)
}