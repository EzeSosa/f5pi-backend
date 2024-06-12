package com.esosa.f5pi_backend.controllers.interfaces

import com.esosa.f5pi_backend.controllers.responses.FieldResponse
import com.esosa.f5pi_backend.controllers.responses.GameResponse
import com.esosa.f5pi_backend.controllers.responses.PlayerResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.UUID

@RequestMapping("api/v1/users")
@Tag(
    name = "Users",
    description = "Allows registered users to fetch their players, games and fields."
)
interface IUserController {
    @GetMapping("/{userId}/players")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Fetches a registered user players")
    fun getUserPlayers(@PathVariable userId: UUID): List<PlayerResponse>

    @GetMapping("/{userId}/games")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Fetches a registered user games")
    fun getUserGames(@PathVariable userId: UUID): List<GameResponse>

    @GetMapping("/{userId}/fields")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Fetches a registered user fields")
    fun getUserFields(@PathVariable userId: UUID): List<FieldResponse>
}