package com.esosa.f5pi_backend.data.models

import com.esosa.f5pi_backend.controllers.responses.GameResponse
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
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
    val season: Season,

    @ManyToOne
    val user: User,

    @OneToOne(cascade = [CascadeType.ALL])
    val details: GameDetails = GameDetails(),

    @Id
    val id: UUID = UUID.randomUUID()
) {
    fun buildGameResponse(): GameResponse = GameResponse(id, date, official, individualPrice, field.name)
}