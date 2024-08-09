package com.esosa.f5pi_backend.services.implementations

import com.esosa.f5pi_backend.controllers.requests.CreatePlayerRequest
import com.esosa.f5pi_backend.controllers.requests.UpdatePlayerRequest
import com.esosa.f5pi_backend.controllers.responses.PlayerResponse
import com.esosa.f5pi_backend.controllers.responses.PlayerStatisticsResponse
import com.esosa.f5pi_backend.data.models.Player
import com.esosa.f5pi_backend.data.repositories.IPlayerRepository
import com.esosa.f5pi_backend.data.models.User
import com.esosa.f5pi_backend.services.interfaces.*
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Service
class PlayerService(
    private val playerRepository: IPlayerRepository,
    private val userService: IUserService,
    private val fieldService: IFieldService,
    private val seasonService: ISeasonService,
    private val fileUploadService: IFileUploadService
) : IPlayerService {

    override fun getPlayerStatistics(playerId: UUID, fieldId: UUID?, seasonId: UUID?): PlayerStatisticsResponse {
        val field = fieldId?.let { fieldService.findFieldByIdOrThrowException(it) }
        val season = seasonId?.let { seasonService.findSeasonByIdOrThrowException(it) }
        return try { playerRepository.getPlayerStatistics(findPlayerByIdOrThrowException(playerId), field, season) }
        catch (e: Exception) { PlayerStatisticsResponse() }
    }

    override fun savePlayer(createPlayerRequest: CreatePlayerRequest): PlayerResponse =
        with(createPlayerRequest) {
            val user = userService.findUserByIdOrThrowException(userId)
            val imageURL = image?.let { uploadPlayerImage(it) }
            playerRepository.save(buildPlayer(user, imageURL))
                .buildPlayerResponse()
        }

    override fun updatePlayer(playerId: UUID, updatePlayerRequest: UpdatePlayerRequest): PlayerResponse =
        findPlayerByIdOrThrowException(playerId).let { player ->
            playerRepository.save(player.apply {
                name = updatePlayerRequest.name
                imageURL = updatePlayerRequest.imageURL
            }).buildPlayerResponse()
        }

    override fun deletePlayer(playerId: UUID) {
        ifPlayerDoesNotExistThrowException(playerId)
        playerRepository.deleteById(playerId)
    }

    override fun findPlayerByIdOrThrowException(playerId: UUID): Player =
        playerRepository.findById(playerId)
            .orElseThrow { ResponseStatusException(HttpStatus.BAD_REQUEST, "Player with id $playerId does not exist") }

    private fun uploadPlayerImage(multiPartFile: MultipartFile): String =
        fileUploadService.uploadFile(multiPartFile)

    private fun ifPlayerDoesNotExistThrowException(playerId: UUID) {
        if (!playerRepository.existsById(playerId))
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Player with id $playerId does not exist")
    }

    private fun CreatePlayerRequest.buildPlayer(user: User, imageURL: String?): Player = Player(name, user, imageURL)
}