package com.esosa.f5pi_backend.security.entrypoint

import org.springframework.security.core.AuthenticationException

class JWTAuthException : AuthenticationException {
    constructor(message: String, cause: Throwable) : super(message, cause)
}