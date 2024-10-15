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

    @Transactional
    override fun saveGameDetails(gameId: UUID, gameDetailsRequest: GameDetailsRequest): GameDetailsResponse {
        ifMembersSizeFromTeamDoesNotEqualThrowException(gameDetailsRequest)

        val game = findGameByIdOrThrowException(gameId)
        val teamGoals = calculateTeamGoals(gameDetailsRequest.teams)
        val teamResults = determineTeamResults(teamGoals)

        gameDetailsRequest.teams.forEachIndexed { index, team ->
            teamService.saveTeam(game.details, teamResults.toList()[index], teamGoals.toList()[index], team)
        }

        return game.details
            .buildGameDetailsResponse()
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

    private fun calculateTeamGoals(teams: List<TeamRequest>): Pair<Int, Int> {
        val team1Goals = teams[0].members.sumOf { it.goalsScored } + teams[1].members.sumOf { it.ownGoals }
        val team2Goals = teams[1].members.sumOf { it.goalsScored } + teams[0].members.sumOf { it.ownGoals }
        return team1Goals to team2Goals
    }

    private fun determineTeamResults(teamGoals: Pair<Int, Int>): Pair<TeamResult, TeamResult> =
        when {
            teamGoals.first > teamGoals.second -> TeamResult.WIN to TeamResult.LOSS
            teamGoals.second > teamGoals.first -> TeamResult.LOSS to TeamResult.WIN
            else -> TeamResult.DRAW to TeamResult.DRAW
        }

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