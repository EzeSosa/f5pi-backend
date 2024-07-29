package com.esosa.f5pi_backend.controllers.responses

data class PlayerStatisticsResponse(
    val games: Long = 0,
    val goals: Long = 0,
    val ownGoals: Long = 0,
    val wins: Long = 0,
    val draws: Long = 0,
    val losses: Long = 0,
    val moneySpent: Long = 0
)