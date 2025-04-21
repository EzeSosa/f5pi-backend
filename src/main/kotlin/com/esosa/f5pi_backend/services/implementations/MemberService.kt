package com.esosa.f5pi_backend.services.implementations

import com.esosa.f5pi_backend.controllers.requests.MemberRequest
import com.esosa.f5pi_backend.controllers.responses.MemberResponse
import com.esosa.f5pi_backend.data.models.Member
import com.esosa.f5pi_backend.data.models.Player
import com.esosa.f5pi_backend.data.repositories.IMemberRepository
import com.esosa.f5pi_backend.services.interfaces.IMemberService
import com.esosa.f5pi_backend.services.interfaces.IPlayerService
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

import java.util.UUID

@Service
class MemberService(
    private val memberRepository: IMemberRepository,
    private val playerService: IPlayerService
) : IMemberService {

    @Async
    override fun saveMember(memberRequest: MemberRequest): MemberResponse =
        with (memberRequest) {
            buildMember(getPlayer(playerId))
                .also { memberRepository.save(it) }
                .buildMemberResponse()
        }

    private fun getPlayer(playerId: UUID) =
        playerService.findPlayerByIdOrThrowException(playerId)

    private fun MemberRequest.buildMember(player: Player): Member =
        Member(goalsScored, ownGoals, player)
}