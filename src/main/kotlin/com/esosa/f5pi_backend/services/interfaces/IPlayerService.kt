package com.esosa.f5pi_backend.services.interfaces

import com.esosa.f5pi_backend.controllers.requests.CreatePlayerRequest
import com.esosa.f5pi_backend.controllers.requests.UpdatePlayerRequest
import com.esosa.f5pi_backend.controllers.responses.PlayerResponse
import com.esosa.f5pi_backend.data.models.Player
import org.springframework.scheduling.annotation.Async
import java.util.UUID

interface IPlayerService {
    fun savePlayer(createPlayerRequest: CreatePlayerRequest): PlayerResponse
    fun updatePlayer(playerId: UUID, updatePlayerRequest: UpdatePlayerRequest): PlayerResponse
    fun deletePlayer(playerId: UUID)
    fun findPlayerByIdOrThrowException(playerId: UUID): Player
    @Async
    fun updatePlayerStatistics(player: Player, goalsScored: Int, winner: Boolean, official: Boolean)
}