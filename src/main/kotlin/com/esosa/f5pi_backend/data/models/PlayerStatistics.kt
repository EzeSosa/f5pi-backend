package com.esosa.f5pi_backend.data.models

import com.esosa.f5pi_backend.controllers.responses.PlayerStatisticsResponse
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.UUID

@Entity
data class PlayerStatistics(
    var allGames: Int = 0,
    var allGoals: Int = 0,
    var allWins: Int = 0,
    var officialGames: Int = 0,
    var officialGoals: Int = 0,
    var officialWins: Int = 0,
    var moneySpent: Double = 0.0,

    @Id
    val id: UUID = UUID.randomUUID()
) {
    fun buildPlayerStatisticsResponse(): PlayerStatisticsResponse = PlayerStatisticsResponse(allGames, allGoals, allWins, officialGames, officialGoals, officialWins)
}