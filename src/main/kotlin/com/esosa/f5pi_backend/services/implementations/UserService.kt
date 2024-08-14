package com.esosa.f5pi_backend.services.implementations

import com.esosa.f5pi_backend.controllers.requests.UpdateUserRequest
import com.esosa.f5pi_backend.controllers.responses.FieldResponse
import com.esosa.f5pi_backend.controllers.responses.GameResponse
import com.esosa.f5pi_backend.controllers.responses.PlayerResponse
import com.esosa.f5pi_backend.controllers.responses.SeasonResponse
import com.esosa.f5pi_backend.controllers.responses.UserResponse
import com.esosa.f5pi_backend.data.models.User
import com.esosa.f5pi_backend.data.repositories.IUserRepository
import com.esosa.f5pi_backend.services.interfaces.*
import org.springframework.context.annotation.Lazy
import org.springframework.data.domain.Page
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
    @Lazy private val seasonService: ISeasonService,
    @Lazy private val playerService: IPlayerService
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

    override fun getUserPlayers(userId: UUID, pageNumber: Int, pageSize: Int): Page<PlayerResponse> =
        playerService.getUserPlayers(
            findUserByIdOrThrowException(userId),
            pageNumber,
            pageSize
        )

    override fun getUserGames(
        userId: UUID,
        dateFrom: LocalDate?,
        dateTo: LocalDate?,
        fieldId: UUID?,
        seasonId: UUID?
    ): List<GameResponse> =
        findUserByIdOrThrowException(userId).let { user ->
            val field = fieldId?.let { fieldService.findFieldByIdOrThrowException(it) }
            val season = seasonId?.let { seasonService.findSeasonByIdOrThrowException(it) }
            gameService.getGamesByUser(user, dateFrom, dateTo, field, season)
        }

    override fun getUserFields(userId: UUID, pageNumber: Int, pageSize: Int): Page<FieldResponse> =
        fieldService.getUserFields(
            findUserByIdOrThrowException(userId),
            pageNumber,
            pageSize
        )

    override fun getUserSeasons(userId: UUID, pageNumber: Int, pageSize: Int): Page<SeasonResponse> =
        seasonService.getUserSeasons(
            findUserByIdOrThrowException(userId),
            pageNumber,
            pageSize
        )

    override fun updateUser(userId: UUID, updateUserRequest: UpdateUserRequest): UserResponse =
        findUserByIdOrThrowException(userId).let { user ->
            userRepository.save(
                user.apply {
                    fullName = updateUserRequest.fullName
                    email = updateUserRequest.email
                }).buildUserResponse()
        }

    override fun deleteUser(userId: UUID) {
        ifUserDoesNotExistThrowException(userId)
        userRepository.deleteById(userId)
    }

    private fun ifUserDoesNotExistThrowException(userId: UUID) {
        if (!userRepository.existsById(userId))
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "User with id $userId does not exist")
    }
}