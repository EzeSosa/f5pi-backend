package com.esosa.f5pi_backend.data.models

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

    @Id
    val id: UUID = UUID.randomUUID()
)