package com.esosa.f5pi_backend.data.models

import com.esosa.f5pi_backend.controllers.responses.GameDetailsResponse
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import java.util.UUID

@Entity
data class GameDetails(
    @OneToMany(mappedBy = "gameDetails", cascade = [CascadeType.ALL])
    val teams: List<Team> = emptyList(),

    @Id
    val id: UUID = UUID.randomUUID()
) {
    fun buildGameDetailsResponse(): GameDetailsResponse = GameDetailsResponse(teams.map(Team::buildTeamResponse))
}