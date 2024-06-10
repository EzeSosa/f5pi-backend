package com.esosa.f5pi_backend.controllers.interfaces

import com.esosa.f5pi_backend.controllers.requests.CreateGameRequest
import com.esosa.f5pi_backend.controllers.requests.GameDetailsRequest
import com.esosa.f5pi_backend.controllers.requests.UpdateGameRequest
import com.esosa.f5pi_backend.controllers.responses.GameDetailsResponse
import com.esosa.f5pi_backend.controllers.responses.GameResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.UUID

@RequestMapping("api/v1/games")
interface IGameController {
    @GetMapping("/{gameId}/detail")
    @ResponseStatus(HttpStatus.OK)
    fun getGameDetails(@PathVariable gameId: UUID): GameDetailsResponse

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun saveGame(@RequestBody @Valid createGameRequest: CreateGameRequest): GameResponse

    @PostMapping("/{gameId}/detail")
    @ResponseStatus(HttpStatus.CREATED)
    fun saveGameDetails(@PathVariable gameId: UUID, @RequestBody @Valid gameDetailsRequest: GameDetailsRequest)

    @PatchMapping("/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    fun updateGame(@PathVariable gameId: UUID, @RequestBody @Valid updateGameRequest: UpdateGameRequest): GameResponse

    @DeleteMapping("/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteGame(@PathVariable gameId: UUID)
}