package com.esosa.f5pi_backend.security.jwt

import com.esosa.f5pi_backend.data.enums.TokenType
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.util.*

@Service
class JWTService(jwtProperties: JWTProperties) : IJWTService {

    private val secretKey = Keys.hmacShaKeyFor(
        jwtProperties.key.toByteArray()
    )

    override fun generateToken(username: String, expirationDate: Date, extraClaims: Map<String, Any>): String =
        Jwts.builder()
            .claims(extraClaims)
            .subject(username)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(expirationDate)
            .signWith(secretKey)
            .compact()

    override fun extractUsernameFromToken(token: String): String =
        getAllClaimsFromToken(token).subject

    override fun extractTokenTypeFromToken(token: String): TokenType =
        getAllClaimsFromToken(token)["tokenType"].let { TokenType.valueOf(it.toString()) }

    override fun isTokenValid(token: String, username: String): Boolean =
        !isTokenExpired(token) && subjectEqualsUsername(token, username)

    override fun isTokenExpired(token: String): Boolean =
        getAllClaimsFromToken(token)
            .expiration
            .before(Date(System.currentTimeMillis()))

    private fun subjectEqualsUsername(token: String, username: String): Boolean =
        getAllClaimsFromToken(token).subject == username

    private fun getAllClaimsFromToken(token: String): Claims =
        Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
}