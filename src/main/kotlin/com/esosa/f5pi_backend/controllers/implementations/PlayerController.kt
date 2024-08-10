package com.esosa.f5pi_backend.controllers.implementations

import com.esosa.f5pi_backend.controllers.interfaces.IPlayerController
import com.esosa.f5pi_backend.controllers.requests.CreatePlayerRequest
import com.esosa.f5pi_backend.controllers.requests.UpdatePlayerRequest
import com.esosa.f5pi_backend.controllers.responses.PlayerResponse
import com.esosa.f5pi_backend.controllers.responses.PlayerStatisticsResponse
import com.esosa.f5pi_backend.services.interfaces.IPlayerService
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@RestController
class PlayerController(private val playerService: IPlayerService) : IPlayerController {
    override fun getPlayerStatistics(
        @PathVariable playerId: UUID,
        @RequestParam fieldId: UUID?,
        @RequestParam seasonId: UUID?
    ): PlayerStatisticsResponse =
        playerService.getPlayerStatistics(playerId, fieldId, seasonId)

    override fun savePlayer(createPlayerRequest: CreatePlayerRequest): PlayerResponse =
        playerService.savePlayer(createPlayerRequest)

    override fun savePlayerImage(playerId: UUID, multiPartFile: MultipartFile) =
        playerService.savePlayerImage(playerId, multiPartFile)

    override fun updatePlayer(playerId: UUID, updatePlayerRequest: UpdatePlayerRequest): PlayerResponse =
        playerService.updatePlayer(playerId, updatePlayerRequest)

    override fun deletePlayer(playerId: UUID) =
        playerService.deletePlayer(playerId)
}