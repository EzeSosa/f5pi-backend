package com.esosa.f5pi_backend.controllers.responses

data class PlayerStatisticsResponse(
    val allGames: Long = 0,
    val allGoals: Long = 0,
    val allWins: Long = 0,
    val officialGames: Long = 0,
    val officialGoals: Long = 0,
    val officialWins: Long = 0,
    val moneySpent: Long = 0
)