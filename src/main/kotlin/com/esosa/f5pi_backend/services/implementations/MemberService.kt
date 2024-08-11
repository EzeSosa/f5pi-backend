package com.esosa.f5pi_backend.services.implementations

import com.esosa.f5pi_backend.controllers.requests.MemberRequest
import com.esosa.f5pi_backend.data.models.Member
import com.esosa.f5pi_backend.data.models.Player
import com.esosa.f5pi_backend.data.models.Team
import com.esosa.f5pi_backend.data.repositories.IMemberRepository
import com.esosa.f5pi_backend.services.interfaces.IMemberService
import com.esosa.f5pi_backend.services.interfaces.IPlayerService
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: IMemberRepository,
    private val playerService: IPlayerService
) : IMemberService {

    override fun saveMember(team: Team, memberRequest: MemberRequest) {
        val player = playerService.findPlayerByIdOrThrowException(memberRequest.playerId)
        memberRepository.save(memberRequest.buildMember(team, player))
            .also { updateTeam(it, team) }
    }

    @Async
    private fun updateTeam(member: Member, team: Team) {
        team.members.add(member)
    }

    private fun MemberRequest.buildMember(team: Team, player: Player): Member = Member(goalsScored, ownGoals, team, player)
}