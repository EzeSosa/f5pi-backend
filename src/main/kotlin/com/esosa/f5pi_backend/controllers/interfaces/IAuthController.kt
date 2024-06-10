package com.esosa.f5pi_backend.controllers.interfaces

import com.esosa.f5pi_backend.controllers.requests.AuthRequest
import com.esosa.f5pi_backend.controllers.responses.LoginResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus

@RequestMapping("/auth")
interface IAuthController {
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestBody @Valid authRequest: AuthRequest)

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    fun login(@RequestBody @Valid authRequest: AuthRequest): LoginResponse
}