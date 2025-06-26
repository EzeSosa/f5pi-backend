package com.esosa.f5pi_backend.services.implementations

import com.esosa.f5pi_backend.controllers.requests.CreateGameRequest
import com.esosa.f5pi_backend.controllers.requests.GameDetailsRequest
import com.esosa.f5pi_backend.controllers.requests.TeamRequest
import com.esosa.f5pi_backend.controllers.requests.UpdateGameRequest
import com.esosa.f5pi_backend.controllers.responses.GameDetailsResponse
import com.esosa.f5pi_backend.controllers.responses.GameResponse
import com.esosa.f5pi_backend.data.enums.TeamResult
import com.esosa.f5pi_backend.data.models.Field
import com.esosa.f5pi_backend.data.models.Game
import com.esosa.f5pi_backend.data.models.Season
import com.esosa.f5pi_backend.data.models.User
import com.esosa.f5pi_backend.data.repositories.IGameRepository
import com.esosa.f5pi_backend.data.repositories.IGameDetailsRepository
import com.esosa.f5pi_backend.data.models.GameDetails
import com.esosa.f5pi_backend.services.interfaces.IFieldService
import com.esosa.f5pi_backend.services.interfaces.IGameService
import com.esosa.f5pi_backend.services.interfaces.ISeasonService
import com.esosa.f5pi_backend.services.interfaces.ITeamService
import com.esosa.f5pi_backend.services.interfaces.IUserService
import com.esosa.f5pi_backend.utils.PageMapper
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.util.UUID

@Service
class GameService(
    private val gameRepository: IGameRepository,
    private val gameDetailsRepository: IGameDetailsRepository,
    private val userService: IUserService,
    private val teamService: ITeamService,
    private val fieldService: IFieldService,
    private val seasonService: ISeasonService
) : IGameService {

    override fun getGameDetails(gameId: UUID): GameDetailsResponse =
        findGameByIdOrThrowException(gameId)
            .details
            .buildGameDetailsResponse()

    @Transactional
    override fun saveGame(createGameRequest: CreateGameRequest): GameResponse =
        with(createGameRequest) {
            val season = seasonService.findSeasonByIdOrThrowException(seasonId)
            ifGameDateIsOutOfSeasonThrowException(date, season)

            val field = fieldService.findFieldByIdOrThrowException(fieldId)
            val user = userService.findUserByIdOrThrowException(userId)

            return buildGame(field, user, season)
                .also { gameRepository.save(it) }
                .buildGameResponse()
        }

    @Transactional
    override fun saveGameDetails(gameId: UUID, gameDetailsRequest: GameDetailsRequest): GameDetailsResponse =
        gameDetailsRequest.let {
            ifMembersSizeFromTeamDoesNotEqualThrowException(it)

            val game = findGameByIdOrThrowException(gameId)
            val teamGoals = calculateTeamGoals(it.teams)
            val teamResults = determineTeamResults(teamGoals)

            // Usar los detalles existentes del juego
            val gameDetails = game.details

            return gameDetails
                .buildGameDetailsResponse()
                .apply {
                    teams = gameDetailsRequest.teams.mapIndexed { index, team ->
                        teamService.saveTeam(teamResults.toList()[index], teamGoals.toList()[index], team, gameDetails)
                    }
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
        season: Season?,
        pageNumber: Int,
        pageSize: Int,
        sortAttribute: String,
        sortOrder: String
    ): Page<GameResponse> =
        gameRepository.findByUser(
            PageMapper.buildPageRequest(pageNumber, pageSize, sortAttribute, sortOrder),
            user,
            dateFrom,
            dateTo,
            field,
            season
        ).map(Game::buildGameResponse)

    fun calculateTeamGoals(teams: List<TeamRequest>): Pair<Int, Int> {
        val team1Goals = teams[0].members.sumOf { it.goalsScored } + teams[1].members.sumOf { it.ownGoals }
        val team2Goals = teams[1].members.sumOf { it.goalsScored } + teams[0].members.sumOf { it.ownGoals }
        return team1Goals to team2Goals
    }

    fun determineTeamResults(teamGoals: Pair<Int, Int>): Pair<TeamResult, TeamResult> =
        when {
            teamGoals.first > teamGoals.second -> TeamResult.WIN to TeamResult.LOSS
            teamGoals.second > teamGoals.first -> TeamResult.LOSS to TeamResult.WIN
            else -> TeamResult.DRAW to TeamResult.DRAW
        }

    private fun ifGameDoesNotExistThrowException(gameId: UUID) {
        if (!gameRepository.existsById(gameId))
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Game with id $gameId does not exist")
    }

    fun ifMembersSizeFromTeamDoesNotEqualThrowException(gameDetailsRequest: GameDetailsRequest) =
        with(gameDetailsRequest) {
            if (teams[0].members.size != teams[1].members.size)
                throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Both teams must have the same member size")
        }

    private fun ifGameDateIsOutOfSeasonThrowException(gameDate: LocalDate, season: Season) {
        if (gameDate.isAfter(season.finalDate))
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Game date is out of the season selected")
    }

    private fun CreateGameRequest.buildGame(field: Field, user: User, season: Season) =
        Game(date, individualPrice, field, season, user)
}