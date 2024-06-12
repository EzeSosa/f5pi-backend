package com.esosa.f5pi_backend.controllers.interfaces

import com.esosa.f5pi_backend.controllers.requests.AuthRequest
import com.esosa.f5pi_backend.controllers.responses.LoginResponse
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
    fun register(@RequestBody @Valid authRequest: AuthRequest)

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Authenticates a new user")
    fun login(@RequestBody @Valid authRequest: AuthRequest): LoginResponse
}