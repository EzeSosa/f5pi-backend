package com.esosa.f5pi_backend.data.models

import com.esosa.f5pi_backend.controllers.responses.TeamResponse
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import java.util.UUID

@Entity
data class Team(
    val winner: Boolean,

    @ManyToOne
    val game: Game,

    @OneToMany(mappedBy = "team")
    val members: List<Member> = emptyList(),

    @Id
    val id: UUID = UUID.randomUUID()
) {
    fun buildTeamResponse(): TeamResponse = TeamResponse(members.map(Member::buildMemberResponse), winner)
}