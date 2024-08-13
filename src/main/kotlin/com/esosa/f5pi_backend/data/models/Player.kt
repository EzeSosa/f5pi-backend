package com.esosa.f5pi_backend.data.models

import com.esosa.f5pi_backend.controllers.responses.PlayerResponse
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import java.time.LocalDateTime
import java.util.UUID

@Entity
data class Player(
    var name: String,

    @ManyToOne
    val user: User,

    var imageURL: String,

    val createdAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "player", cascade = [CascadeType.ALL])
    val memberOf: List<Member> = emptyList(),

    @Id
    val id: UUID = UUID.randomUUID()
) {
    fun buildPlayerResponse(): PlayerResponse = PlayerResponse(id, name, imageURL)
}