package com.esosa.f5pi_backend.services.implementations

import com.esosa.f5pi_backend.controllers.requests.CreatePlayerRequest
import com.esosa.f5pi_backend.controllers.requests.UpdatePlayerRequest
import com.esosa.f5pi_backend.controllers.responses.PlayerResponse
import com.esosa.f5pi_backend.controllers.responses.PlayerStatisticsResponse
import com.esosa.f5pi_backend.data.models.Player
import com.esosa.f5pi_backend.data.repositories.IPlayerRepository
import com.esosa.f5pi_backend.services.interfaces.IPlayerService
import com.esosa.f5pi_backend.services.interfaces.IUserService
import com.esosa.f5pi_backend.data.models.User
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class PlayerService(
    private val playerRepository: IPlayerRepository,
    private val userService: IUserService
) : IPlayerService {

    override fun getPlayerStatistics(playerId: UUID): PlayerStatisticsResponse =
        try { playerRepository.getPlayerStatistics(findPlayerByIdOrThrowException(playerId)) }
        catch (e: Exception) { PlayerStatisticsResponse() }

    override fun savePlayer(createPlayerRequest: CreatePlayerRequest): PlayerResponse =
        with(createPlayerRequest) {
            val user = userService.findUserByIdOrThrowException(userId)
            playerRepository.save(buildPlayer(user))
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

    private fun ifPlayerDoesNotExistThrowException(playerId: UUID) {
        if (!playerRepository.existsById(playerId))
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Player with id $playerId does not exist")
    }

    private fun CreatePlayerRequest.buildPlayer(user: User): Player = Player(name, user, imageURL)
}