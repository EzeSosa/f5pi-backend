package com.esosa.f5pi_backend.services.implementations

import com.esosa.f5pi_backend.controllers.requests.AuthRequest
import com.esosa.f5pi_backend.controllers.requests.RefreshTokenRequest
import com.esosa.f5pi_backend.controllers.responses.LoginResponse
import com.esosa.f5pi_backend.controllers.responses.RefreshTokenResponse
import com.esosa.f5pi_backend.data.models.User
import com.esosa.f5pi_backend.security.jwt.JWTProperties
import com.esosa.f5pi_backend.security.jwt.JWTService
import com.esosa.f5pi_backend.services.interfaces.IAuthService
import com.esosa.f5pi_backend.services.interfaces.IUserService
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.Date
import java.util.UUID

@Service
class AuthService(
    private val userService: IUserService,
    private val userDetailsService: UserDetailsService,
    private val passwordEncoder: PasswordEncoder,
    private val authManager: AuthenticationManager,
    private val jwtService: JWTService,
    private val jwtProperties: JWTProperties
): IAuthService {

    override fun register(authRequest: AuthRequest) {
        with(authRequest) {
            validateExistsUsername(username)
            userService.saveUser(buildUser())
        }
    }

    override fun login(authRequest: AuthRequest): LoginResponse =
        with(authRequest) {
            authManager.authenticate(UsernamePasswordAuthenticationToken(username, password))

            val user = userDetailsService.loadUserByUsername(username)
            val accessToken = generateAccessToken(user)
            val refreshToken = generateRefreshToken(user)

            LoginResponse(username.extractId(), accessToken, refreshToken)
        }

    override fun refreshToken(refreshTokenRequest: RefreshTokenRequest): RefreshTokenResponse =
        with(refreshTokenRequest) {
            jwtService.extractUsernameFromToken(refreshToken).let { username ->
                val currentUserDetails = userDetailsService.loadUserByUsername(username)

                if (!jwtService.isTokenValid(refreshToken, currentUserDetails))
                    throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh token is not valid")

                RefreshTokenResponse(generateAccessToken(currentUserDetails))
            }
        }

    private fun generateAccessToken(userDetails: UserDetails): String =
        jwtService.generateToken(
            userDetails,
            Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration)
        )

    private fun generateRefreshToken(userDetails: UserDetails): String =
        jwtService.generateToken(
            userDetails,
            Date(System.currentTimeMillis() + jwtProperties.refreshTokenExpiration)
        )

    private fun AuthRequest.buildUser(): User = User(username, passwordEncoder.encode(password))
    private fun String.extractId(): UUID = userService.findUserByUsernameOrThrowException(this).id
    private fun validateExistsUsername(username: String) = userService.ifExistsUsernameThrowException(username)
}