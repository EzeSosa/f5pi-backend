package com.esosa.f5pi_backend.services.interfaces

import com.esosa.f5pi_backend.controllers.requests.AuthRequest
import com.esosa.f5pi_backend.controllers.requests.CheckTokenRequest
import com.esosa.f5pi_backend.controllers.requests.RefreshTokenRequest
import com.esosa.f5pi_backend.controllers.responses.LoginResponse
import com.esosa.f5pi_backend.controllers.responses.RefreshTokenResponse
import com.esosa.f5pi_backend.controllers.responses.UserResponse

interface IAuthService {
    fun register(authRequest: AuthRequest)
    fun login(authRequest: AuthRequest): LoginResponse
    fun refreshToken(refreshTokenRequest: RefreshTokenRequest): RefreshTokenResponse
    fun checkToken(checkTokenRequest: CheckTokenRequest): UserResponse
}