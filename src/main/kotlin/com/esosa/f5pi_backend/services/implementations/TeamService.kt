package com.esosa.f5pi_backend.services.implementations

import com.esosa.f5pi_backend.controllers.requests.TeamRequest
import com.esosa.f5pi_backend.data.models.Game
import com.esosa.f5pi_backend.data.models.Team
import com.esosa.f5pi_backend.data.repositories.ITeamRepository
import com.esosa.f5pi_backend.services.interfaces.IMemberService
import com.esosa.f5pi_backend.services.interfaces.ITeamService
import org.springframework.stereotype.Service

@Service
class TeamService(
    private val teamRepository: ITeamRepository,
    private val memberService: IMemberService
) : ITeamService {

    override fun saveTeam(game: Game, teamRequest: TeamRequest, official: Boolean) {
        teamRepository.save(teamRequest.buildTeam(game)).let { team ->
            teamRequest.members
                .forEach { memberRequest -> memberService.saveMember(team, memberRequest, teamRequest.winner, official) }
        }
    }

    private fun TeamRequest.buildTeam(game: Game): Team = Team(winner, game)
}