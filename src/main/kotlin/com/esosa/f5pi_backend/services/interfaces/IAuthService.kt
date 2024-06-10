package com.esosa.f5pi_backend.services.interfaces

import com.esosa.f5pi_backend.controllers.requests.AuthRequest
import com.esosa.f5pi_backend.controllers.responses.LoginResponse

interface IAuthService {
    fun register(authRequest: AuthRequest)
    fun login(authRequest: AuthRequest): LoginResponse
}