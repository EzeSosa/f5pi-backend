package com.esosa.f5pi_backend.services.implementations

import com.esosa.f5pi_backend.controllers.requests.GameDetailsRequest
import com.esosa.f5pi_backend.controllers.requests.MemberRequest
import com.esosa.f5pi_backend.controllers.requests.TeamRequest
import com.esosa.f5pi_backend.data.enums.TeamResult
import com.esosa.f5pi_backend.data.models.Field
import com.esosa.f5pi_backend.data.models.Game
import com.esosa.f5pi_backend.data.models.Season
import com.esosa.f5pi_backend.data.models.User
import com.esosa.f5pi_backend.data.repositories.IGameRepository
import com.esosa.f5pi_backend.services.interfaces.IFieldService
import com.esosa.f5pi_backend.services.interfaces.ISeasonService
import com.esosa.f5pi_backend.services.interfaces.ITeamService
import com.esosa.f5pi_backend.services.interfaces.IUserService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.isNull
import org.springframework.data.domain.PageImpl
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.util.UUID

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

    @Test
    fun getGamesByUser() {
        val testGames = listOf(
            Game(LocalDate.now(), 2000.0, Field("testField", User()),
                Season("testSeason", LocalDate.now(), LocalDate.now(), User()), User()),
        )
        val gamesPage = PageImpl(testGames)

        `when`(gameRepository.findByUser(
            any(),
            any(),
            isNull(),
            isNull(),
            isNull(),
            isNull()
        )).thenReturn(gamesPage)

        val user = User()
        val result = gameService.getGamesByUser(
            user = user,
            pageNumber = 0,
            pageSize = 10,
            sortAttribute = "date",
            sortOrder = "asc"
        )

        assertEquals(result.numberOfElements, 1)
    }
}