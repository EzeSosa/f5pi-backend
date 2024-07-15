package com.esosa.f5pi_backend.services.interfaces

import com.esosa.f5pi_backend.controllers.responses.FieldResponse
import com.esosa.f5pi_backend.controllers.responses.GameResponse
import com.esosa.f5pi_backend.controllers.responses.PlayerResponse
import com.esosa.f5pi_backend.controllers.responses.SeasonResponse
import com.esosa.f5pi_backend.data.models.User
import java.time.LocalDate
import java.util.UUID

interface IUserService {
    fun saveUser(user: User): User
    fun ifExistsUsernameThrowException(username: String)
    fun findUserByUsernameOrThrowException(username: String): User
    fun findUserByIdOrThrowException(userId: UUID): User
    fun getUserPlayers(userId: UUID): List<PlayerResponse>
    fun getUserGames(
        userId: UUID,
        dateFrom: LocalDate?,
        dateTo: LocalDate?,
        official: Boolean?,
        fieldId: UUID?,
        seasonId: UUID?
    ): List<GameResponse>
    fun getUserFields(userId: UUID): List<FieldResponse>
    fun getUserSeasons(userId: UUID): List<SeasonResponse>
}