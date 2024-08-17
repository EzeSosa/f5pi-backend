package com.esosa.f5pi_backend.controllers.implementations

import com.esosa.f5pi_backend.controllers.interfaces.IUserController
import com.esosa.f5pi_backend.controllers.requests.UpdateUserRequest
import com.esosa.f5pi_backend.controllers.responses.FieldResponse
import com.esosa.f5pi_backend.controllers.responses.GameResponse
import com.esosa.f5pi_backend.controllers.responses.PlayerResponse
import com.esosa.f5pi_backend.controllers.responses.SeasonResponse
import com.esosa.f5pi_backend.controllers.responses.UserResponse
import com.esosa.f5pi_backend.services.interfaces.IUserService
import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.util.UUID

@RestController
class UserController(private val userService: IUserService) : IUserController {
    override fun getUserPlayers(userId: UUID, pageNumber: Int, pageSize: Int, sortAttribute: String, sortOrder: String): Page<PlayerResponse> =
        userService.getUserPlayers(userId, pageNumber, pageSize, sortAttribute, sortOrder)

    override fun getUserGames(
        userId: UUID,
        dateFrom: LocalDate?,
        dateTo: LocalDate?,
        fieldId: UUID?,
        seasonId: UUID?,
        pageNumber: Int,
        pageSize: Int,
        sortAttribute: String,
        sortOrder: String
    ): Page<GameResponse> =
        userService.getUserGames(userId, dateFrom, dateTo, fieldId, seasonId, pageNumber, pageSize, sortAttribute, sortOrder)

    override fun getUserFields(userId: UUID, pageNumber: Int, pageSize: Int): Page<FieldResponse> =
        userService.getUserFields(userId, pageNumber, pageSize)

    override fun getUserSeasons(userId: UUID, pageNumber: Int, pageSize: Int): Page<SeasonResponse> =
        userService.getUserSeasons(userId, pageNumber, pageSize)

    override fun updateUser(userId: UUID, updateUserRequest: UpdateUserRequest): UserResponse =
        userService.updateUser(userId, updateUserRequest)

    override fun deleteUser(userId: UUID) =
        userService.deleteUser(userId)
}