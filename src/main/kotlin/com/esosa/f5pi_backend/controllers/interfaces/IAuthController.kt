package com.esosa.f5pi_backend.controllers.interfaces

import com.esosa.f5pi_backend.controllers.requests.RegisterRequest
import com.esosa.f5pi_backend.controllers.requests.CheckTokenRequest
import com.esosa.f5pi_backend.controllers.requests.LoginRequest
import com.esosa.f5pi_backend.controllers.requests.RefreshTokenRequest
import com.esosa.f5pi_backend.controllers.responses.LoginResponse
import com.esosa.f5pi_backend.controllers.responses.RefreshTokenResponse
import com.esosa.f5pi_backend.controllers.responses.UserResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus

@RequestMapping("/auth")
@Tag(
    name = "Authentication",
    description = "Allows a user to register and registered users to login."
)
interface IAuthController {
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registers a new user")
    fun register(@RequestBody @Valid registerRequest: RegisterRequest)

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Authenticates an existent user")
    fun login(@RequestBody @Valid loginRequest: LoginRequest): LoginResponse

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Refreshes the access token for an existent user")
    fun refresh(@RequestBody @Valid refreshTokenRequest: RefreshTokenRequest): RefreshTokenResponse

    @PostMapping("/check-token")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Retrieves user information over an access token")
    fun checkToken(@RequestBody @Valid checkTokenRequest: CheckTokenRequest): UserResponse
}