package com.esosa.f5pi_backend.controllers.interfaces

import com.esosa.f5pi_backend.controllers.requests.UpdateUserRequest
import com.esosa.f5pi_backend.controllers.responses.FieldResponse
import com.esosa.f5pi_backend.controllers.responses.GameResponse
import com.esosa.f5pi_backend.controllers.responses.PlayerResponse
import com.esosa.f5pi_backend.controllers.responses.SeasonResponse
import com.esosa.f5pi_backend.controllers.responses.UserResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import java.time.LocalDate
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
    fun getUserGames(
        @PathVariable userId: UUID,
        @RequestParam dateFrom: LocalDate?,
        @RequestParam dateTo: LocalDate?,
        @RequestParam official: Boolean?,
        @RequestParam fieldId: UUID?,
        @RequestParam seasonId: UUID?
    ): List<GameResponse>

    @GetMapping("/{userId}/fields")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Fetches a registered user fields")
    fun getUserFields(@PathVariable userId: UUID): List<FieldResponse>

    @GetMapping("/{userId}/seasons")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Fetches a registered user seasons")
    fun getUserSeasons(@PathVariable userId: UUID): List<SeasonResponse>

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Updates a registered user data")
    fun updateUser(@PathVariable userId: UUID, @RequestBody updateUserRequest: UpdateUserRequest): UserResponse

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes a registered user")
    fun deleteUser(@PathVariable userId: UUID)
}