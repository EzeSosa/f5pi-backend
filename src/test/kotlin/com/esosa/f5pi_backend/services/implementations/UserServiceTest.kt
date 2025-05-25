package com.esosa.f5pi_backend.services.implementations

import com.esosa.f5pi_backend.controllers.responses.FieldResponse
import com.esosa.f5pi_backend.controllers.responses.GameResponse
import com.esosa.f5pi_backend.controllers.responses.PlayerResponse
import com.esosa.f5pi_backend.controllers.responses.SeasonResponse
import com.esosa.f5pi_backend.data.enums.Role
import com.esosa.f5pi_backend.data.models.Field
import com.esosa.f5pi_backend.data.models.Player
import com.esosa.f5pi_backend.data.models.Season
import com.esosa.f5pi_backend.data.models.User
import com.esosa.f5pi_backend.data.repositories.IUserRepository
import com.esosa.f5pi_backend.services.interfaces.IFieldService
import com.esosa.f5pi_backend.services.interfaces.IGameService
import com.esosa.f5pi_backend.services.interfaces.IPlayerService
import com.esosa.f5pi_backend.services.interfaces.ISeasonService

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.springframework.data.domain.PageImpl
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.TemporalQueries.localDate
import java.util.Optional
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class UserServiceTest {
    @Mock
    lateinit var userRepository: IUserRepository
    @Mock
    lateinit var gameService: IGameService
    @Mock
    lateinit var fieldService: IFieldService
    @Mock
    lateinit var seasonService: ISeasonService
    @Mock
    lateinit var playerService: IPlayerService
    @InjectMocks
    lateinit var userService: UserService

    @Test
    fun saveUser() {
        val user = User(
            username = "testUser",
            password = "testPassword",
            fullName = "testFullName",
            email = "test@email.com",
            role = Role.USER,
        )
        `when`(userRepository.save(user)).thenReturn(user)

        val result = userService.saveUser(user)

        assertEquals(user.username,result.username)
        assertEquals(user.password, result.password)
    }
    @Test
    fun getUserPlayers() {
        val user = User(
            username = "testUser",
            password = "testPassword",
            fullName = "testFullName",
            email = "test@email.com",
            role = Role.USER,
        )
        `when`(userRepository.save(user)).thenReturn(user)

        val result1 = userService.saveUser(user)

        val testPlayers = listOf(
            PlayerResponse(playerId = UUID.randomUUID(), name = "testPlayer", imageURL = "www.url.com"),
            PlayerResponse(playerId = UUID.randomUUID(), name = "testPlayer2", imageURL = "www.url2.com"),
        )
        val playersPage = PageImpl(testPlayers)
        `when`(userRepository.findById(result1.id)).thenReturn(Optional.of(result1))
        `when`(playerService.getUserPlayers(
            result1,
            0,
            10,
            "name",
            "asc"))
            .thenReturn(playersPage)


        val result2 = userService.getUserPlayers(
            user.id,
            0,
            10,
            "name",
            "asc"
        )
        assertEquals(2,result2.numberOfElements)
    }

    @Test
    fun getUserGames() {
        val user = User(
            username = "testUser",
            password = "testPassword",
            fullName = "testFullName",
            email = "test@email.com",
            role = Role.USER,
        )
        `when`(userRepository.save(user)).thenReturn(user)

        val result1 = userService.saveUser(user)


        val field = Field(
            name = "testField",
            user = user,
            id = UUID.randomUUID(),
        )
        `when`( fieldService.findFieldByIdOrThrowException(field.id)).thenReturn(field)

        val season = Season(
            name = "testSeason",
            initialDate = LocalDate.now().minusDays(30),
            finalDate = LocalDate.now(),
            user = user,
            createdAt = LocalDateTime.now(),
            id = UUID.randomUUID()
        )
        `when`(seasonService.findSeasonByIdOrThrowException(season.id)).thenReturn(season)


        val testGames = listOf(
            GameResponse(UUID.randomUUID(), LocalDate.now(), 1235.00, "Almagro", "2025" ),
            GameResponse(UUID.randomUUID(), LocalDate.now().minusDays(1), 1235.00, "Alumni", "2025" ),
        )

        val gamesPage = PageImpl(testGames)

        `when`(userRepository.findById(result1.id)).thenReturn(Optional.of(result1))
        `when`(gameService.getGamesByUser(
            result1,
            LocalDate.now().minusDays(1),
            LocalDate.now(),
            field,
            season,
            0,
            10,
            "name",
            "asc"
            )
        ).thenReturn(gamesPage)

        val result2 = userService.getUserGames(
            user.id,
            LocalDate.now().minusDays(1),
            LocalDate.now(),
            field.id,
            season.id,
            0,
            10,
           "name",
            "asc"
            );

        assertEquals(2,result2.numberOfElements)
    }

    @Test
    fun getUserFields() {
        val user = User(
            username = "testUser",
            password = "testPassword",
            fullName = "testFullName",
            email = "test@email.com",
            role = Role.USER,
        )
        `when`(userRepository.save(user)).thenReturn(user)

        val result1 = userService.saveUser(user)

        `when`(userRepository.findById(result1.id)).thenReturn(Optional.of(result1))

        val testFields = listOf(
            FieldResponse(
                fieldId = UUID.randomUUID(),
                fieldName = "testField",
            ),
            FieldResponse(
                fieldId = UUID.randomUUID(),
                fieldName = "testField2",
            )
        )

        val fieldsPage = PageImpl(testFields)

        `when`(fieldService.getUserFields(
            result1,
            0,
            10,
            "name",
            "asc"
        )).thenReturn(fieldsPage)

        val result2 = userService.getUserFields(
            user.id,
            0,
            10,
            "name",
            "asc"
        )

        assertEquals(2,result2.numberOfElements)
    }

    @Test
    fun getUserSeasons() {
        val user = User(
            username = "testUser",
            password = "testPassword",
            fullName = "testFullName",
            email = "test@email.com",
            role = Role.USER,
        )
        `when`(userRepository.save(user)).thenReturn(user)

        val result1 = userService.saveUser(user)

        `when`(userRepository.findById(result1.id)).thenReturn(Optional.of(result1))

        val testSeasons =  listOf(
            SeasonResponse(id = UUID.randomUUID(), name = "2025", initialDate = LocalDate.now().minusDays(30), finalDate = LocalDate.now()),
            SeasonResponse(id = UUID.randomUUID(), name = "2024", initialDate = LocalDate.now().minusDays(30), finalDate = LocalDate.now())
        )

        val seasonsPage = PageImpl(testSeasons)

        `when`(seasonService.getUserSeasons(
            result1,
            0,
            10,
            "name",
            "asc"
        )).thenReturn(seasonsPage)

        val result2 = userService.getUserSeasons(
            user.id,
            0,
            10,
            "name",
            "asc"
        )

        assertEquals(2,result2.numberOfElements)
    }

    @Test
    fun updateUser() {
        val user = User(
            username = "testUser",
            password = "testPassword",
            fullName = "testFullName",
            email = "test@email.com",
            role = Role.USER
        )
        `when`(userRepository.save(user)).thenReturn(user)

        val result1 = userService.saveUser(user)

        val updateUserRequest = User(
            username = "testUser",
            password = "testPassword",
            fullName = "testFullName",
            email = "test2@email.com",


    }

    @Test
    fun deleteUser() {
    }

}