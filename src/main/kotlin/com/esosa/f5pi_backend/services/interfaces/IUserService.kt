package com.esosa.f5pi_backend.services.interfaces

import com.esosa.f5pi_backend.controllers.requests.UpdateUserRequest
import com.esosa.f5pi_backend.controllers.responses.FieldResponse
import com.esosa.f5pi_backend.controllers.responses.GameResponse
import com.esosa.f5pi_backend.controllers.responses.PlayerResponse
import com.esosa.f5pi_backend.controllers.responses.SeasonResponse
import com.esosa.f5pi_backend.controllers.responses.UserResponse
import com.esosa.f5pi_backend.data.models.User
import org.springframework.data.domain.Page
import java.time.LocalDate
import java.util.UUID

interface IUserService {
    fun saveUser(user: User): User
    fun ifExistsUsernameThrowException(username: String)
    fun findUserByUsernameOrThrowException(username: String): User
    fun findUserByIdOrThrowException(userId: UUID): User
    fun getUserPlayers(userId: UUID, pageNumber: Int, pageSize: Int): Page<PlayerResponse>
    fun getUserGames(
        userId: UUID,
        dateFrom: LocalDate?,
        dateTo: LocalDate?,
        fieldId: UUID?,
        seasonId: UUID?
    ): List<GameResponse>
    fun getUserFields(userId: UUID, pageNumber: Int, pageSize: Int): Page<FieldResponse>
    fun getUserSeasons(userId: UUID, pageNumber: Int, pageSize: Int): Page<SeasonResponse>
    fun updateUser(userId: UUID, updateUserRequest: UpdateUserRequest): UserResponse
    fun deleteUser(userId: UUID)
}