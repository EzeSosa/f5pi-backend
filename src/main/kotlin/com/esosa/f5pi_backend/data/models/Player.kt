package com.esosa.f5pi_backend.data.models

import com.esosa.f5pi_backend.controllers.responses.PlayerResponse
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import java.util.UUID

@Entity
data class Player(
    var name: String,

    @ManyToOne
    val user: User,

    @OneToOne(cascade = [CascadeType.ALL])
    val playerStatistics: PlayerStatistics = PlayerStatistics(),

    @OneToMany(mappedBy = "player")
    val memberOf: List<Member> = emptyList(),

    @Id
    val id: UUID = UUID.randomUUID()
) {
    fun buildPlayerResponse(): PlayerResponse = PlayerResponse(id, name, playerStatistics)
}