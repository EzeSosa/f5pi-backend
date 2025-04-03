package com.esosa.f5pi_backend.services.implementations

import com.esosa.f5pi_backend.controllers.requests.GameDetailsRequest
import com.esosa.f5pi_backend.controllers.requests.MemberRequest
import com.esosa.f5pi_backend.controllers.requests.TeamRequest
import com.esosa.f5pi_backend.data.enums.TeamResult
import com.esosa.f5pi_backend.data.repositories.IGameRepository
import com.esosa.f5pi_backend.services.interfaces.IFieldService
import com.esosa.f5pi_backend.services.interfaces.ISeasonService
import com.esosa.f5pi_backend.services.interfaces.ITeamService
import com.esosa.f5pi_backend.services.interfaces.IUserService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.web.server.ResponseStatusException
import java.util.*

@ExtendWith(MockitoExtension::class)
class GameServiceTest {
    @Mock lateinit var gameRepository: IGameRepository
    @Mock lateinit var userService: IUserService
    @Mock lateinit var teamService: ITeamService
    @Mock lateinit var fieldService: IFieldService
    @Mock lateinit var seasonService: ISeasonService

    @InjectMocks
    lateinit var gameService: GameService

    @Test
    fun calculateTeamGoals() {
        val teamRequestList = listOf(
            TeamRequest(
                members = listOf(
                    MemberRequest(playerId = UUID.randomUUID(), goalsScored = 3, ownGoals = 1),
                    MemberRequest(playerId = UUID.randomUUID(), goalsScored = 5, ownGoals = 2)
                )
            ),
            TeamRequest(
                members = listOf(
                    MemberRequest(playerId = UUID.randomUUID(), goalsScored = 7, ownGoals = 2),
                    MemberRequest(playerId = UUID.randomUUID(), goalsScored = 4, ownGoals = 1),
                ),
            )
        )

        val teamGoals = gameService.calculateTeamGoals(teamRequestList)
        assertEquals(teamGoals, 11 to 14)
    }

    @Test
    fun determineTeamResults() {
        val teamGoals = 6 to 3
        val teamResults = gameService.determineTeamResults(teamGoals)
        assertEquals(teamResults, TeamResult.WIN to TeamResult.LOSS)
    }

    @Test
    fun ifMembersSizeFromTeamDoesNotEqualThrowException() {
        val detailsRequest = GameDetailsRequest(
            teams = listOf(
                TeamRequest(
                    members = listOf(
                        MemberRequest(playerId = UUID.randomUUID(), goalsScored = 3, ownGoals = 1),
                        MemberRequest(playerId = UUID.randomUUID(), goalsScored = 5, ownGoals = 2)
                    )
                ),
                TeamRequest(
                    members = listOf(
                        MemberRequest(playerId = UUID.randomUUID(), goalsScored = 7, ownGoals = 2),
                        MemberRequest(playerId = UUID.randomUUID(), goalsScored = 4, ownGoals = 1),
                        MemberRequest(playerId = UUID.randomUUID(), goalsScored = 4, ownGoals = 1)
                    )
                )
            )
        )

        val exception = assertThrows<ResponseStatusException> {
            gameService.ifMembersSizeFromTeamDoesNotEqualThrowException(detailsRequest)
        }

        assertEquals("400 BAD_REQUEST \"Both teams must have the same member size\"", exception.message)
    }
}