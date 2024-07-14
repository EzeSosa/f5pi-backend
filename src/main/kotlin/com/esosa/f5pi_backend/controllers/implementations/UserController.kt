package com.esosa.f5pi_backend.controllers.implementations

import com.esosa.f5pi_backend.controllers.interfaces.IUserController
import com.esosa.f5pi_backend.controllers.responses.FieldResponse
import com.esosa.f5pi_backend.controllers.responses.GameResponse
import com.esosa.f5pi_backend.controllers.responses.PlayerResponse
import com.esosa.f5pi_backend.controllers.responses.SeasonResponse
import com.esosa.f5pi_backend.services.interfaces.IUserService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.util.UUID

@RestController
class UserController(private val userService: IUserService) : IUserController {
    override fun getUserPlayers(userId: UUID): List<PlayerResponse> =
        userService.getUserPlayers(userId)

    override fun getUserGames(
        @PathVariable userId: UUID,
        @RequestParam dateFrom: LocalDate?,
        @RequestParam dateTo: LocalDate?,
        @RequestParam official: Boolean?
    ): List<GameResponse> =
        userService.getUserGames(userId, dateFrom, dateTo, official)

    override fun getUserFields(userId: UUID): List<FieldResponse> =
        userService.getUserFields(userId)

    override fun getUserSeasons(userId: UUID): List<SeasonResponse> =
        userService.getUserSeasons(userId)
}