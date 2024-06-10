package com.esosa.f5pi_backend.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.Date

@Service
class JWTService(jwtProperties: JWTProperties) {
    private val secretKey = Keys.hmacShaKeyFor(
        jwtProperties.key.toByteArray()
    )

    fun generateToken(userDetails: UserDetails, expirationDate: Date): String =
        Jwts.builder()
            .claims()
            .subject(userDetails.username)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(expirationDate)
            .and()
            .signWith(secretKey)
            .compact()

    fun extractUsernameFromToken(token: String): String =
        getAllClaimsFromToken(token).subject

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean =
        !isTokenExpired(token) && subjectEqualsUsername(token, userDetails)

    private fun subjectEqualsUsername(token: String, userDetails: UserDetails): Boolean =
        getAllClaimsFromToken(token)
            .subject == userDetails.username

    private fun isTokenExpired(token: String): Boolean =
        getAllClaimsFromToken(token)
            .expiration
            .before(Date(System.currentTimeMillis()))

    private fun getAllClaimsFromToken(token: String): Claims =
        Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
}