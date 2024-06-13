package com.esosa.f5pi_backend.data.models

import com.esosa.f5pi_backend.controllers.responses.GameDetailsResponse
import com.esosa.f5pi_backend.controllers.responses.GameResponse
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import java.time.LocalDate
import java.util.UUID

@Entity
data class Game(
    var date: LocalDate,
    var official: Boolean,
    var individualPrice: Double,

    @ManyToOne
    val field: Field,

    @ManyToOne
    val user: User,

    @OneToMany(mappedBy = "game", cascade = [CascadeType.ALL])
    val teams: List<Team> = emptyList(),

    @Id
    val id: UUID = UUID.randomUUID()
) {
    fun buildGameResponse(): GameResponse = GameResponse(id, date, official, individualPrice, field.name)
    fun buildGameDetailsResponse(): GameDetailsResponse = GameDetailsResponse(teams.map(Team::buildTeamResponse))
}