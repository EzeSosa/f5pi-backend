package com.esosa.f5pi_backend.data.models

import com.esosa.f5pi_backend.controllers.responses.SeasonResponse
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import java.time.LocalDate
import java.util.UUID

@Entity
data class Season(
    var name: String,
    var initialDate: LocalDate,
    var finalDate: LocalDate,

    @ManyToOne
    val user: User,

    @OneToMany(mappedBy = "season")
    val games: List<Game> = emptyList(),

    @Id
    val id: UUID = UUID.randomUUID()
) {
    fun buildSeasonResponse() = SeasonResponse(name, initialDate, finalDate)
}