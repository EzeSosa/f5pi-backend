package com.esosa.f5pi_backend.services.implementations

import com.esosa.f5pi_backend.controllers.responses.PlayerResponse
import com.esosa.f5pi_backend.data.enums.Role
import com.esosa.f5pi_backend.data.models.Player
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
import java.time.LocalDateTime
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

    }

    @Test
    fun getUserFields() {
    }

    @Test
    fun getUserSeasons() {
    }

    @Test
    fun updateUser() {
    }

    @Test
    fun deleteUser() {
    }

}