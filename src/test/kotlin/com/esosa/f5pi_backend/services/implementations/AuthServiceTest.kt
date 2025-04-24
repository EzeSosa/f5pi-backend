package com.esosa.f5pi_backend.services.implementations

import com.esosa.f5pi_backend.controllers.requests.RegisterRequest
import com.esosa.f5pi_backend.data.models.User
import com.esosa.f5pi_backend.security.jwt.JWTProperties
import com.esosa.f5pi_backend.security.jwt.JWTService
import com.esosa.f5pi_backend.services.interfaces.IUserService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder

@ExtendWith(MockitoExtension::class)
class AuthServiceTest {
    @Mock
    lateinit var userService: IUserService

    @Mock
    lateinit var userDetailService: UserDetailsService

    @Mock
    lateinit var passwordEncoder: PasswordEncoder

    @Mock
    lateinit var authManager: AuthenticationManager

    @Mock
    lateinit var jwtService: JWTService

    @Mock
    lateinit var jwtProperties: JWTProperties

    @InjectMocks
    lateinit var authService: AuthService

    @Test
    fun register() {
        val registerRequest = RegisterRequest(
            username = "fran",
            password = "123456",
            fullName = "Francisco Morano",
            email = "moranofrancisco1234@gmail.com"
        )

        val encodedPassword = "encodedPassword"
        `when`(passwordEncoder.encode(registerRequest.password)).thenReturn(encodedPassword)

        authService.register(registerRequest)

        argumentCaptor<User>().run {
            verify(userService).saveUser(capture())
            firstValue
        }.also { savedUser ->
            with(registerRequest) {
                assertEquals(username, savedUser.username)
                assertEquals(encodedPassword, savedUser.password)
                assertEquals(fullName, savedUser.fullName)
                assertEquals(email, savedUser.email)
            }
        }
    }
}