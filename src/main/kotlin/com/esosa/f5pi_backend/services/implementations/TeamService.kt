package com.esosa.f5pi_backend.services.implementations

import com.esosa.f5pi_backend.controllers.requests.MemberRequest
import com.esosa.f5pi_backend.controllers.requests.TeamRequest
import com.esosa.f5pi_backend.controllers.responses.MemberResponse
import com.esosa.f5pi_backend.controllers.responses.TeamResponse
import com.esosa.f5pi_backend.data.enums.TeamResult
import com.esosa.f5pi_backend.data.models.Team
import com.esosa.f5pi_backend.data.models.GameDetails
import com.esosa.f5pi_backend.data.repositories.ITeamRepository
import com.esosa.f5pi_backend.services.interfaces.IMemberService
import com.esosa.f5pi_backend.services.interfaces.ITeamService
import org.springframework.stereotype.Service

@Service
class TeamService(
    private val teamRepository: ITeamRepository,
    private val memberService: IMemberService
) : ITeamService {

    override fun saveTeam(
        teamResult: TeamResult,
        teamGoals: Int,
        teamRequest: TeamRequest,
        gameDetails: GameDetails
    ): TeamResponse =
        buildTeam(teamResult, teamGoals, gameDetails)
            .let { team ->
                val savedTeam = teamRepository.save(team)
                TeamResponse(
                    result = savedTeam.result,
                    goals = savedTeam.goals,
                    members = saveMembers(teamRequest.members, savedTeam)
                )
            }

    private fun buildTeam(teamResult: TeamResult, teamGoals: Int, gameDetails: GameDetails): Team =
        Team(teamResult, teamGoals, gameDetails)

    private fun saveMembers(members: List<MemberRequest>, team: Team): List<MemberResponse> =
        members.map { memberRequest -> memberService.saveMember(memberRequest, team) }
}