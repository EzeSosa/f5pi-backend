package com.esosa.f5pi_backend.data.models

import com.esosa.f5pi_backend.controllers.responses.TeamResponse
import com.esosa.f5pi_backend.data.enums.TeamResult
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import java.util.UUID

@Entity
data class Team(
    @Enumerated(value = EnumType.STRING)
    val result: TeamResult,
    val goals: Int,

    @ManyToOne
    val gameDetails: GameDetails,

    @OneToMany(mappedBy = "team", cascade = [CascadeType.ALL])
    val members: MutableList<Member> = mutableListOf(),

    @Id
    val id: UUID = UUID.randomUUID()
) {
    fun buildTeamResponse(): TeamResponse = TeamResponse(result, goals, members.map(Member::buildMemberResponse))
}