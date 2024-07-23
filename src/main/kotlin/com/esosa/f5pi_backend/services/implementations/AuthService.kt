package com.esosa.f5pi_backend.services.implementations

import com.esosa.f5pi_backend.controllers.requests.RegisterRequest
import com.esosa.f5pi_backend.controllers.requests.CheckTokenRequest
import com.esosa.f5pi_backend.controllers.requests.LoginRequest
import com.esosa.f5pi_backend.controllers.requests.RefreshTokenRequest
import com.esosa.f5pi_backend.controllers.responses.LoginResponse
import com.esosa.f5pi_backend.controllers.responses.RefreshTokenResponse
import com.esosa.f5pi_backend.controllers.responses.UserResponse
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

@Service
class AuthService(
    private val userService: IUserService,
    private val userDetailsService: UserDetailsService,
    private val passwordEncoder: PasswordEncoder,
    private val authManager: AuthenticationManager,
    private val jwtService: JWTService,
    private val jwtProperties: JWTProperties
): IAuthService {

    override fun register(registerRequest: RegisterRequest) {
        with(registerRequest) {
            validateExistsUsername(username)
            userService.saveUser(buildUser())
        }
    }

    override fun login(loginRequest: LoginRequest): LoginResponse =
        with(loginRequest) {
            authManager.authenticate(UsernamePasswordAuthenticationToken(username, password))

            val user = userDetailsService.loadUserByUsername(username)
            val accessToken = generateAccessToken(user)
            val refreshToken = generateRefreshToken(user)

            LoginResponse(username.extractUser().buildUserResponse(), accessToken, refreshToken)
        }

    override fun refreshToken(refreshTokenRequest: RefreshTokenRequest): RefreshTokenResponse =
        with(refreshTokenRequest) {
            jwtService.extractUsernameFromToken(refreshToken).let { username ->
                val currentUserDetails = userDetailsService.loadUserByUsername(username)
                ifTokenInvalidThrowException(refreshToken, currentUserDetails)
                RefreshTokenResponse(generateAccessToken(currentUserDetails))
            }
        }

    override fun checkToken(checkTokenRequest: CheckTokenRequest): UserResponse =
        with(checkTokenRequest) {
            val username = jwtService.extractUsernameFromToken(accessToken)
            val userDetails = userDetailsService.loadUserByUsername(username)
            ifTokenInvalidThrowException(accessToken, userDetails)
            userService.findUserByUsernameOrThrowException(username)
                .buildUserResponse()
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

    private fun ifTokenInvalidThrowException(token: String, userDetails: UserDetails) {
        if (!jwtService.isTokenValid(token, userDetails))
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh token is not valid")
    }

    private fun RegisterRequest.buildUser(): User = User(username, passwordEncoder.encode(password), fullName, email)
    private fun String.extractUser(): User = userService.findUserByUsernameOrThrowException(this)
    private fun validateExistsUsername(username: String) = userService.ifExistsUsernameThrowException(username)
}