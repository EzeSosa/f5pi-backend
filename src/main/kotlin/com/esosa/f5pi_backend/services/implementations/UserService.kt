package com.esosa.f5pi_backend.services.implementations

import com.esosa.f5pi_backend.controllers.responses.FieldResponse
import com.esosa.f5pi_backend.controllers.responses.GameResponse
import com.esosa.f5pi_backend.controllers.responses.PlayerResponse
import com.esosa.f5pi_backend.controllers.responses.SeasonResponse
import com.esosa.f5pi_backend.data.models.Field
import com.esosa.f5pi_backend.data.models.Player
import com.esosa.f5pi_backend.data.models.Season
import com.esosa.f5pi_backend.data.models.User
import com.esosa.f5pi_backend.data.repositories.IUserRepository
import com.esosa.f5pi_backend.services.interfaces.IFieldService
import com.esosa.f5pi_backend.services.interfaces.IGameService
import com.esosa.f5pi_backend.services.interfaces.ISeasonService
import com.esosa.f5pi_backend.services.interfaces.IUserService
import org.springframework.context.annotation.Lazy
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.util.UUID

@Service
class UserService(
    private val userRepository: IUserRepository,
    @Lazy private val gameService: IGameService,
    @Lazy private val fieldService: IFieldService,
    @Lazy private val seasonService: ISeasonService
) : IUserService {

    override fun saveUser(user: User): User =
        userRepository.save(user)

    override fun ifExistsUsernameThrowException(username: String) {
        if (userRepository.existsByUsername(username))
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists")
    }

    override fun findUserByUsernameOrThrowException(username: String): User =
        userRepository.findByUsername(username)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Username does not exist")

    override fun findUserByIdOrThrowException(userId: UUID): User =
        userRepository.findById(userId)
            .orElseThrow{ ResponseStatusException(HttpStatus.BAD_REQUEST, "Username with id $userId does not exist") }

    override fun getUserPlayers(userId: UUID): List<PlayerResponse> =
        findUserByIdOrThrowException(userId)
            .players
            .map(Player::buildPlayerResponse)

    override fun getUserGames(
        userId: UUID,
        dateFrom: LocalDate?,
        dateTo: LocalDate?,
        official: Boolean?,
        fieldId: UUID?,
        seasonId: UUID?
    ): List<GameResponse> =
        findUserByIdOrThrowException(userId).let { user ->
            val field = fieldId?.let { fieldService.findFieldByIdOrThrowException(it) }
            val season = seasonId?.let { seasonService.findSeasonByIdOrThrowException(it) }
            gameService.getGamesByUser(user, dateFrom, dateTo, field, season)
        }

    override fun getUserFields(userId: UUID): List<FieldResponse> =
        findUserByIdOrThrowException(userId)
            .fields
            .map(Field::buildFieldResponse)

    override fun getUserSeasons(userId: UUID): List<SeasonResponse> =
        findUserByIdOrThrowException(userId)
            .seasons
            .map(Season::buildSeasonResponse)
}