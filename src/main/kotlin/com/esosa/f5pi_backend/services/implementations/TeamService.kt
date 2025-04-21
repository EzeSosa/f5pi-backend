package com.esosa.f5pi_backend.services.implementations

import com.esosa.f5pi_backend.controllers.requests.MemberRequest
import com.esosa.f5pi_backend.controllers.requests.TeamRequest
import com.esosa.f5pi_backend.controllers.responses.MemberResponse
import com.esosa.f5pi_backend.controllers.responses.TeamResponse
import com.esosa.f5pi_backend.data.enums.TeamResult
import com.esosa.f5pi_backend.data.models.Team
import com.esosa.f5pi_backend.data.repositories.ITeamRepository
import com.esosa.f5pi_backend.services.interfaces.IMemberService
import com.esosa.f5pi_backend.services.interfaces.ITeamService
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class TeamService(
    private val teamRepository: ITeamRepository,
    private val memberService: IMemberService
) : ITeamService {

    @Async
    override fun saveTeam(
        teamResult: TeamResult,
        teamGoals: Int,
        teamRequest: TeamRequest
    ) =
        buildTeam(teamResult, teamGoals)
            .also { teamRepository.save(it) }
            .let { TeamResponse(it.result, it.goals, saveMembers(teamRequest.members)) }

    private fun buildTeam(teamResult: TeamResult, teamGoals: Int): Team =
        Team(teamResult, teamGoals)

    private fun saveMembers(members: List<MemberRequest>): List<MemberResponse> =
        members.map { memberRequest -> memberService.saveMember(memberRequest) }
}