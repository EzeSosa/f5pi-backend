package com.esosa.f5pi_backend.services.implementations

import com.esosa.f5pi_backend.controllers.requests.MemberRequest
import com.esosa.f5pi_backend.data.models.Member
import com.esosa.f5pi_backend.data.models.Player
import com.esosa.f5pi_backend.data.models.Team
import com.esosa.f5pi_backend.data.repositories.IMemberRepository
import com.esosa.f5pi_backend.services.interfaces.IMemberService
import com.esosa.f5pi_backend.services.interfaces.IPlayerService
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: IMemberRepository,
    private val playerService: IPlayerService
) : IMemberService {

    override fun saveMember(team: Team, memberRequest: MemberRequest, winner: Boolean, official: Boolean, price: Double) {
        val player = playerService.findPlayerByIdOrThrowException(memberRequest.playerId)
        memberRepository.save(memberRequest.buildMember(team, player))
    }

    private fun MemberRequest.buildMember(team: Team, player: Player): Member = Member(goalsScored, team, player)
}