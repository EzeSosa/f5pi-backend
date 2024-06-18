package com.esosa.f5pi_backend.controllers.responses

data class PlayerStatisticsResponse(
    var allGames: Int = 0,
    var allGoals: Int = 0,
    var allWins: Int = 0,
    var officialGames: Int = 0,
    var officialGoals: Int = 0,
    var officialWins: Int = 0
)