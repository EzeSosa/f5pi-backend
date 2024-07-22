package com.esosa.f5pi_backend.controllers.implementations

import com.esosa.f5pi_backend.controllers.interfaces.IAuthController
import com.esosa.f5pi_backend.controllers.requests.AuthRequest
import com.esosa.f5pi_backend.controllers.requests.CheckTokenRequest
import com.esosa.f5pi_backend.controllers.requests.RefreshTokenRequest
import com.esosa.f5pi_backend.controllers.responses.LoginResponse
import com.esosa.f5pi_backend.controllers.responses.RefreshTokenResponse
import com.esosa.f5pi_backend.controllers.responses.UserResponse
import com.esosa.f5pi_backend.services.interfaces.IAuthService
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(private val authService: IAuthService) : IAuthController {
    override fun register(authRequest: AuthRequest) =
        authService.register(authRequest)

    override fun login(authRequest: AuthRequest): LoginResponse =
        authService.login(authRequest)

    override fun refresh(refreshTokenRequest: RefreshTokenRequest): RefreshTokenResponse =
        authService.refreshToken(refreshTokenRequest)

    override fun checkToken(checkTokenRequest: CheckTokenRequest): UserResponse =
        authService.checkToken(checkTokenRequest)
}