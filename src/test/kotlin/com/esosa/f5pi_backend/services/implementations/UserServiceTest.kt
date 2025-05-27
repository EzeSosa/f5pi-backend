package com.esosa.f5pi_backend.services.implementations

import com.esosa.f5pi_backend.controllers.requests.UpdateUserRequest
import com.esosa.f5pi_backend.controllers.responses.FieldResponse
import com.esosa.f5pi_backend.controllers.responses.GameResponse
import com.esosa.f5pi_backend.controllers.responses.PlayerResponse
import com.esosa.f5pi_backend.controllers.responses.SeasonResponse
import com.esosa.f5pi_backend.data.enums.Role
import com.esosa.f5pi_backend.data.models.Field
import com.esosa.f5pi_backend.data.models.Season
import com.esosa.f5pi_backend.data.models.User
import com.esosa.f5pi_backend.data.repositories.IPlayerRepository
import com.esosa.f5pi_backend.data.repositories.IUserRepository
import com.esosa.f5pi_backend.services.interfaces.IFieldService
import com.esosa.f5pi_backend.services.interfaces.IGameService
import com.esosa.f5pi_backend.services.interfaces.IPlayerService
import com.esosa.f5pi_backend.services.interfaces.ISeasonService
import com.esosa.f5pi_backend.utils.PageMapper

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.PageImpl
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class UserServiceTest {

    object UserServiceTestData {
        val TEST_USER = User(
            username = "testUser",
            password = "testPassword",
            fullName = "testFullName",
            email = "test@email.com",
            role = Role.USER,
        )
    }

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

    @Mock
    lateinit var playerRepository: IPlayerRepository

    @InjectMocks
    lateinit var userService: UserService

    @Test
    fun saveUser() {

        `when`(userRepository.save(UserServiceTestData.TEST_USER)).thenReturn(UserServiceTestData.TEST_USER)

        val result = userService.saveUser(UserServiceTestData.TEST_USER)

        assertEquals(UserServiceTestData.TEST_USER.username, result.username)
        assertEquals(UserServiceTestData.TEST_USER.password, result.password)
    }

    @Test
    fun updateUser() {
        val updateUserRequest = UpdateUserRequest(
            fullName = "testFullName2",
            email = "test2@email.com",
        )
        val modifiedUser =
            User("testUser", "testPassword", "testFullName2", "test2@email.com", UserServiceTestData.TEST_USER.role)

        `when`(userRepository.findById(UserServiceTestData.TEST_USER.id)).thenReturn(Optional.of(UserServiceTestData.TEST_USER))
        `when`(userRepository.save(UserServiceTestData.TEST_USER)).thenReturn(modifiedUser)

        userService.updateUser(UserServiceTestData.TEST_USER.id, updateUserRequest)
            .also {
                assertEquals(modifiedUser.fullName, it.fullName)
                assertEquals(modifiedUser.email, it.email)
            }


    }
}