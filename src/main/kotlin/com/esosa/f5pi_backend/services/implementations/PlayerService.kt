package com.esosa.f5pi_backend.services.implementations

import com.esosa.f5pi_backend.controllers.requests.CreatePlayerRequest
import com.esosa.f5pi_backend.controllers.requests.UpdatePlayerRequest
import com.esosa.f5pi_backend.controllers.responses.PlayerResponse
import com.esosa.f5pi_backend.controllers.responses.PlayerStatisticsResponse
import com.esosa.f5pi_backend.controllers.responses.SavePlayerImageResponse
import com.esosa.f5pi_backend.data.models.Player
import com.esosa.f5pi_backend.data.repositories.IPlayerRepository
import com.esosa.f5pi_backend.data.models.User
import com.esosa.f5pi_backend.services.interfaces.*
import com.esosa.f5pi_backend.utils.PageMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
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

    @Value("\${cloudinary.default-image-url}")
    lateinit var DEFAULT_IMAGE_URL: String

    override fun getPlayerStatistics(playerId: UUID, fieldId: UUID?, seasonId: UUID?): PlayerStatisticsResponse {
        val field = fieldId?.let { fieldService.findFieldByIdOrThrowException(it) }
        val season = seasonId?.let { seasonService.findSeasonByIdOrThrowException(it) }
        return try { playerRepository.getPlayerStatistics(findPlayerByIdOrThrowException(playerId), field, season) }
        catch (e: Exception) { PlayerStatisticsResponse() }
    }

    override fun savePlayer(createPlayerRequest: CreatePlayerRequest): PlayerResponse =
        with(createPlayerRequest) {
            val user = userService.findUserByIdOrThrowException(userId)
            ifPlayerNameExistsForUserThrowException(name, user)
            playerRepository.save(buildPlayer(user, DEFAULT_IMAGE_URL))
                .buildPlayerResponse()
        }

    override fun savePlayerImage(playerId: UUID, multiPartFile: MultipartFile): SavePlayerImageResponse =
        findPlayerByIdOrThrowException(playerId).let { player ->
            val imageURL = uploadPlayerImage(multiPartFile)
            playerRepository.save(player.apply { this.imageURL = imageURL })
            return@let SavePlayerImageResponse(imageURL)
        }

    override fun updatePlayer(playerId: UUID, updatePlayerRequest: UpdatePlayerRequest): PlayerResponse =
        findPlayerByIdOrThrowException(playerId).let { player ->
            ifPlayerNameExistsForUserThrowException(updatePlayerRequest.name, player.user)
            playerRepository.save( player.apply { name = updatePlayerRequest.name } )
                .buildPlayerResponse()
        }

    override fun deletePlayer(playerId: UUID) {
        ifPlayerDoesNotExistThrowException(playerId)
        playerRepository.deleteById(playerId)
    }

    override fun findPlayerByIdOrThrowException(playerId: UUID): Player =
        playerRepository.findById(playerId)
            .orElseThrow { ResponseStatusException(HttpStatus.BAD_REQUEST, "Player with id $playerId does not exist") }

    override fun getUserPlayers(user: User, pageNumber: Int, pageSize: Int): Page<PlayerResponse> =
        playerRepository.findByUser( PageMapper.buildPageRequest(pageNumber, pageSize, "createdAt"), user )
            .map(Player::buildPlayerResponse)

    private fun uploadPlayerImage(multiPartFile: MultipartFile): String =
        fileUploadService.uploadFile(multiPartFile)

    private fun ifPlayerNameExistsForUserThrowException(playerName: String, user: User) {
        if (playerRepository.existsByNameAndUser(playerName, user))
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Season with name $playerName already exists for that user")
    }

    private fun ifPlayerDoesNotExistThrowException(playerId: UUID) {
        if (!playerRepository.existsById(playerId))
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Player with id $playerId does not exist")
    }

    private fun CreatePlayerRequest.buildPlayer(user: User, imageURL: String): Player = Player(name, user, imageURL)
}