package com.esosa.f5pi_backend.services.interfaces

import com.esosa.f5pi_backend.controllers.requests.CreatePlayerRequest
import com.esosa.f5pi_backend.controllers.requests.UpdatePlayerRequest
import com.esosa.f5pi_backend.controllers.responses.PlayerResponse
import com.esosa.f5pi_backend.controllers.responses.PlayerStatisticsResponse
import com.esosa.f5pi_backend.controllers.responses.SavePlayerImageResponse
import com.esosa.f5pi_backend.data.models.Player
import com.esosa.f5pi_backend.data.models.User
import org.springframework.data.domain.Page
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

interface IPlayerService {
    fun getPlayerStatistics(
        playerId: UUID,
        fieldId: UUID? = null,
        seasonId: UUID? = null
    ): PlayerStatisticsResponse
    fun savePlayer(createPlayerRequest: CreatePlayerRequest): PlayerResponse
    fun savePlayerImage(playerId: UUID, multiPartFile: MultipartFile): SavePlayerImageResponse
    fun updatePlayer(playerId: UUID, updatePlayerRequest: UpdatePlayerRequest): PlayerResponse
    fun deletePlayer(playerId: UUID)
    fun findPlayerByIdOrThrowException(playerId: UUID): Player
    fun getUserPlayers(user: User, pageNumber: Int, pageSize: Int): Page<PlayerResponse>
}