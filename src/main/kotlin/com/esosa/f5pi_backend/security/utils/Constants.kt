package com.esosa.f5pi_backend.security.utils

import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.HttpStatus.UNAUTHORIZED

class Constants {
    companion object {
        val EXPIRED_JWT_EXCEPTION_MESSAGE = "Session expired. Please log in again" to UNAUTHORIZED
        val SIGNATURE_EXCEPTION_MESSAGE = "Invalid token. Please contact an administrator" to UNAUTHORIZED
        val MISSING_HEADER_EXCEPTION_MESSAGE = "Missing authorization header" to UNAUTHORIZED
        val CLAIMS_JWT_EXCEPTION_MESSAGE = "Token claims are invalid. Please contact an administrator" to FORBIDDEN
        val JWT_EXCEPTION_DEFAULT_MESSAGE = "The JWT authentication failed. Please contact an administrator" to FORBIDDEN
        val USERNAME_NOT_FOUND_MESSAGE = "Username not found. Please contact an administrator" to FORBIDDEN
    }
}