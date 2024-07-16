package com.esosa.f5pi_backend.services.implementations

import com.esosa.f5pi_backend.controllers.requests.CreateGameRequest
import com.esosa.f5pi_backend.controllers.requests.GameDetailsRequest
import com.esosa.f5pi_backend.controllers.requests.UpdateGameRequest
import com.esosa.f5pi_backend.controllers.responses.GameDetailsResponse
import com.esosa.f5pi_backend.controllers.responses.GameResponse
import com.esosa.f5pi_backend.data.enums.TeamResult
import com.esosa.f5pi_backend.data.models.Field
import com.esosa.f5pi_backend.data.models.Game
import com.esosa.f5pi_backend.data.models.Season
import com.esosa.f5pi_backend.data.models.User
import com.esosa.f5pi_backend.data.repositories.IGameRepository
import com.esosa.f5pi_backend.services.interfaces.IFieldService
import com.esosa.f5pi_backend.services.interfaces.IGameService
import com.esosa.f5pi_backend.services.interfaces.ISeasonService
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
    private val seasonService: ISeasonService
) : IGameService {

    override fun getGameDetails(gameId: UUID): GameDetailsResponse =
        findGameByIdOrThrowException(gameId)
            .details
            .buildGameDetailsResponse()

    override fun saveGame(createGameRequest: CreateGameRequest): GameResponse =
        with(createGameRequest) {
            val user = userService.findUserByIdOrThrowException(userId)
            val field = fieldService.findFieldByIdOrThrowException(fieldId)
            val season = seasonService.findSeasonByIdOrThrowException(seasonId)
            ifGameDateIsOutOfSeasonThrowException(this, season)

            gameRepository.save(buildGame(field, user, season))
                .buildGameResponse()
        }

    override fun saveGameDetails(gameId: UUID, gameDetailsRequest: GameDetailsRequest) {
        ifMembersSizeFromTeamDoesNotEqualThrowException(gameDetailsRequest)

        val game = findGameByIdOrThrowException(gameId)
        val (team1Goals, team2Goals) = gameDetailsRequest.teams.map { team -> team.members.sumOf { it.goalsScored } }
        val teamResults = when {
            team1Goals > team2Goals -> TeamResult.WIN to TeamResult.LOSS
            team2Goals > team1Goals -> TeamResult.LOSS to TeamResult.WIN
            else -> TeamResult.DRAW to TeamResult.DRAW
        }

        gameDetailsRequest.teams.forEachIndexed { index, team ->
            teamService.saveTeam(game.details, teamResults.toList()[index], team)
        }
    }

    override fun updateGame(gameId: UUID, updateGameRequest: UpdateGameRequest): GameResponse =
        findGameByIdOrThrowException(gameId).let { game ->
            gameRepository.save(game.apply {
                date = updateGameRequest.date
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

    override fun getGamesByUser(
        user: User,
        dateFrom: LocalDate?,
        dateTo: LocalDate?,
        field: Field?,
        season: Season?
    ): List<GameResponse> =
        gameRepository.findByUser(user, dateFrom, dateTo, field, season)
            .map(Game::buildGameResponse)

    private fun ifGameDoesNotExistThrowException(gameId: UUID) {
        if (!gameRepository.existsById(gameId))
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Game with id $gameId does not exist")
    }

    private fun ifMembersSizeFromTeamDoesNotEqualThrowException(gameDetailsRequest: GameDetailsRequest) {
        with(gameDetailsRequest){
            if (teams[0].members.size != teams[1].members.size)
                throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Both teams must have the same member size")
        }
    }

    private fun ifGameDateIsOutOfSeasonThrowException(createGameRequest: CreateGameRequest, season: Season) {
        if (createGameRequest.date.isAfter(season.finalDate))
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Game date is out of the season selected")
    }

    private fun CreateGameRequest.buildGame(field: Field, user: User, season: Season) =
        Game(date, individualPrice, field, season, user)
}