package com.esosa.f5pi_backend.security.jwt

import com.esosa.f5pi_backend.data.enums.TokenType
import java.util.*

interface IJWTService {
    fun generateToken(username: String, expirationDate: Date, extraClaims: Map<String, Any>): String
    fun extractUsernameFromToken(token: String): String
    fun extractTokenTypeFromToken(token: String): TokenType
    fun isTokenValid(token: String, username: String): Boolean
}