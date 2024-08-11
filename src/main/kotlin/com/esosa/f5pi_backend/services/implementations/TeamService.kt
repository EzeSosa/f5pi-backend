package com.esosa.f5pi_backend.services.implementations

import com.esosa.f5pi_backend.controllers.requests.TeamRequest
import com.esosa.f5pi_backend.data.enums.TeamResult
import com.esosa.f5pi_backend.data.models.GameDetails
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

    override fun saveTeam(gameDetails: GameDetails, teamResult: TeamResult, teamGoals: Int, teamRequest: TeamRequest) {
        teamRepository.save(buildTeam(teamResult, teamGoals, gameDetails))
            .also { updateGameDetails(it, gameDetails) }
            .let { team -> teamRequest.members
                    .forEach { memberRequest -> memberService.saveMember(team, memberRequest) }
        }
    }

    private fun buildTeam(teamResult: TeamResult, teamGoals: Int, gameDetails: GameDetails): Team =
        Team(teamResult, teamGoals, gameDetails)

    @Async
    private fun updateGameDetails(team: Team, gameDetails: GameDetails) {
        gameDetails.teams.add(team)
    }
}