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
    val team: Team,

    @ManyToOne
    val player: Player,

    @Id
    val id: UUID = UUID.randomUUID()
) {
    fun buildMemberResponse(): MemberResponse = MemberResponse(player.name, player.imageURL, goalsScored, ownGoals)
}