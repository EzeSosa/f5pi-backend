package com.esosa.f5pi_backend.controllers.implementations

import com.esosa.f5pi_backend.controllers.interfaces.IUserController
import com.esosa.f5pi_backend.controllers.responses.GameResponse
import com.esosa.f5pi_backend.controllers.responses.PlayerResponse
import com.esosa.f5pi_backend.services.interfaces.IUserService
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class UserController(private val userService: IUserService) : IUserController {
    override fun getUserPlayers(userId: UUID): List<PlayerResponse> =
        userService.getUserPlayers(userId)

    override fun getUserGames(userId: UUID): List<GameResponse> =
        userService.getUserGames(userId)
}