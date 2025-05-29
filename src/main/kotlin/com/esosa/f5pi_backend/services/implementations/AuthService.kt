package com.esosa.f5pi_backend.services.implementations

import com.esosa.f5pi_backend.controllers.requests.CheckTokenRequest
import com.esosa.f5pi_backend.controllers.requests.LoginRequest
import com.esosa.f5pi_backend.controllers.requests.RefreshTokenRequest
import com.esosa.f5pi_backend.controllers.requests.RegisterRequest
import com.esosa.f5pi_backend.controllers.responses.LoginResponse
import com.esosa.f5pi_backend.controllers.responses.RefreshTokenResponse
import com.esosa.f5pi_backend.controllers.responses.UserResponse
import com.esosa.f5pi_backend.data.enums.TokenType
import com.esosa.f5pi_backend.data.enums.TokenType.ACCESS
import com.esosa.f5pi_backend.data.enums.TokenType.REFRESH
import com.esosa.f5pi_backend.data.models.User
import com.esosa.f5pi_backend.security.jwt.IJWTService
import com.esosa.f5pi_backend.security.jwt.JWTProperties
import com.esosa.f5pi_backend.services.interfaces.IAuthService
import com.esosa.f5pi_backend.services.interfaces.IUserService
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class AuthService(
    private val userService: IUserService,
    private val passwordEncoder: PasswordEncoder,
    private val authManager: AuthenticationManager,
    private val jwtService: IJWTService,
    private val jwtProperties: JWTProperties
) : IAuthService {

    override fun register(registerRequest: RegisterRequest) {
        with(registerRequest) {
            validateExistsUsername(username)
            userService.saveUser(buildUser())
        }
    }

    override fun login(loginRequest: LoginRequest): LoginResponse =
        with(loginRequest) {
            authManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
            userService.findUserByUsernameOrThrowException(username).let {
                LoginResponse(
                    it.buildUserResponse(),
                    generateAccessToken(it.username),
                    generateRefreshToken(it.username),
                )
            }
        }

    override fun refreshToken(refreshTokenRequest: RefreshTokenRequest): RefreshTokenResponse =
        with(refreshTokenRequest) {
            ifNotRefreshTokenThrowException(refreshToken)
            ifTokenExpiredThrowException(refreshToken)
            RefreshTokenResponse(
                generateAccessToken(jwtService.extractUsernameFromToken(refreshToken))
            )
        }

    override fun checkToken(checkTokenRequest: CheckTokenRequest): UserResponse =
        with(checkTokenRequest) {
            ifNotAccessTokenThrowException(accessToken)
            ifTokenExpiredThrowException(accessToken)
            jwtService.extractUsernameFromToken(accessToken).let {
                userService.findUserByUsernameOrThrowException(it)
                    .buildUserResponse()
            }
        }

    private fun generateAccessToken(username: String): String =
        jwtService.generateToken(
            username,
            Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration),
            generateTokenTypeClaim(ACCESS)
        )

    private fun generateRefreshToken(username: String): String =
        jwtService.generateToken(
            username,
            Date(System.currentTimeMillis() + jwtProperties.refreshTokenExpiration),
            generateTokenTypeClaim(REFRESH)
        )

    private fun generateTokenTypeClaim(tokenType: TokenType): Map<String, Any> =
        hashMapOf("tokenType" to tokenType)

    private fun ifTokenExpiredThrowException(token: String) {
        if (!jwtService.isTokenExpired(token))
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Token is expired")
    }

    private fun ifNotRefreshTokenThrowException(token: String) {
        if (jwtService.extractTokenTypeFromToken(token) != REFRESH)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Token is not a refresh token")
    }

    private fun ifNotAccessTokenThrowException(token: String) {
        if (jwtService.extractTokenTypeFromToken(token) != ACCESS)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Token is not an access token")
    }

    private fun validateExistsUsername(username: String) =
        userService.ifExistsUsernameThrowException(username)

    private fun RegisterRequest.buildUser(): User =
        User(username, passwordEncoder.encode(password), fullName, email)
}
