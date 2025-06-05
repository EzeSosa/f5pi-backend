package com.esosa.f5pi_backend.security.entrypoint

import com.esosa.f5pi_backend.security.utils.Constants.Companion.JWT_EXCEPTION_DEFAULT_MESSAGE
import org.springframework.security.core.AuthenticationException

class JWTAuthException : AuthenticationException {
    constructor(cause: Throwable): super(JWT_EXCEPTION_DEFAULT_MESSAGE.first, cause)
    constructor(message: String, cause: Throwable) : super(message, cause)
}