package com.esosa.f5pi_backend.services.implementations

import com.esosa.f5pi_backend.controllers.requests.CreatePlayerRequest
import com.esosa.f5pi_backend.controllers.requests.UpdatePlayerRequest
import com.esosa.f5pi_backend.controllers.responses.PlayerResponse
import com.esosa.f5pi_backend.data.models.Player
import com.esosa.f5pi_backend.data.repositories.IPlayerRepository
import com.esosa.f5pi_backend.services.interfaces.IPlayerService
import com.esosa.f5pi_backend.services.interfaces.IUserService
import com.esosa.f5pi_backend.data.models.User
import com.esosa.f5pi_backend.data.repositories.IPlayerStatisticsRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class PlayerService(
    private val playerRepository: IPlayerRepository,
    private val playerStatisticsRepository: IPlayerStatisticsRepository,
    private val userService: IUserService
) : IPlayerService {

    override fun savePlayer(createPlayerRequest: CreatePlayerRequest): PlayerResponse =
        with(createPlayerRequest) {
            val user = userService.findUserByIdOrThrowException(userId)
            playerRepository.save(buildPlayer(user))
                .buildPlayerResponse()
        }

    override fun updatePlayer(playerId: UUID, updatePlayerRequest: UpdatePlayerRequest): PlayerResponse =
        findPlayerByIdOrThrowException(playerId).let { player ->
            playerRepository.save(player.apply { name = updatePlayerRequest.name })
                .buildPlayerResponse()
        }

    override fun deletePlayer(playerId: UUID) {
        ifPlayerDoesNotExistThrowException(playerId)
        playerRepository.deleteById(playerId)
    }

    override fun findPlayerByIdOrThrowException(playerId: UUID): Player =
        playerRepository.findById(playerId)
            .orElseThrow { ResponseStatusException(HttpStatus.BAD_REQUEST, "Player with id $playerId does not exist") }

    override fun updatePlayerStatistics(player: Player, goalsScored: Int, winner: Boolean, official: Boolean) {
        playerStatisticsRepository.save(
            player.playerStatistics.apply {
                if (winner && official) officialWins += 1
                if (winner) allWins += 1
                if (official) officialGoals += goalsScored
                allGoals += goalsScored
            }
        )
    }

    private fun ifPlayerDoesNotExistThrowException(playerId: UUID) {
        if (!playerRepository.existsById(playerId))
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Player with id $playerId does not exist")
    }

    private fun CreatePlayerRequest.buildPlayer(user: User): Player = Player(name, user)
}