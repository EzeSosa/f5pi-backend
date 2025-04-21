package com.esosa.f5pi_backend.data.models

import com.esosa.f5pi_backend.controllers.responses.MemberResponse
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.util.UUID

@Entity
data class Member(
    val goalsScored: Int,
    val ownGoals: Int,

    @ManyToOne
    val player: Player = Player(),

    @ManyToOne
    val team: Team = Team(),

    @Id
    val id: UUID = UUID.randomUUID()
) {
    constructor() : this(0, 0, Player(), Team())

    fun buildMemberResponse(): MemberResponse = MemberResponse(player.name, player.imageURL, goalsScored, ownGoals)
}