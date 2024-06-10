package com.esosa.f5pi_backend.controllers.interfaces

import com.esosa.f5pi_backend.controllers.requests.CreatePlayerRequest
import com.esosa.f5pi_backend.controllers.requests.UpdatePlayerRequest
import com.esosa.f5pi_backend.controllers.responses.PlayerResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.UUID

@RequestMapping("api/v1/players")
interface IPlayerController {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun savePlayer(@RequestBody @Valid createPlayerRequest: CreatePlayerRequest): PlayerResponse

    @PatchMapping("/{playerId}")
    @ResponseStatus(HttpStatus.OK)
    fun updatePlayer(@PathVariable playerId: UUID, @RequestBody @Valid updatePlayerRequest: UpdatePlayerRequest): PlayerResponse

    @DeleteMapping("/{playerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletePlayer(@PathVariable playerId: UUID)
}