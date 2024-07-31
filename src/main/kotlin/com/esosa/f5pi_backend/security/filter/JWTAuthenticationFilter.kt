package com.esosa.f5pi_backend.security.filter

import com.esosa.f5pi_backend.security.jwt.JWTService
import com.esosa.f5pi_backend.security.service.CustomUserDetailsService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter
import com.esosa.f5pi_backend.utils.WHITE_LIST_URL

@Component
class JWTAuthenticationFilter(
    private val userDetailsService: CustomUserDetailsService,
    private val jwtService: JWTService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader: String? = request.getHeader(HttpHeaders.AUTHORIZATION)

        if (!authHeader.containsBearerToken()) {
            filterChain.doFilter(request, response)
            return
        }

        val token = authHeader!!.extractToken()
        val username = jwtService.extractUsernameFromToken(token)

        if (SecurityContextHolder.getContext().authentication == null) {
            val user = userDetailsService.loadUserByUsername(username)

            if (jwtService.isTokenValid(token, user) && jwtService.extractTokenTypeFromToken(token)!! == "ACCESS")
                updateContext(user, request)

            filterChain.doFilter(request, response)
        }
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean =
        WHITE_LIST_URL.any { url -> AntPathMatcher().match(url, request.requestURI) }

    private fun updateContext(user: UserDetails, request: HttpServletRequest) {
        val authToken = UsernamePasswordAuthenticationToken(user, null, user.authorities)
        authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authToken
    }

    private fun String?.containsBearerToken(): Boolean =
        this != null && startsWith("Bearer ")

    private fun String.extractToken(): String =
        substringAfter("Bearer ").takeIf { token -> token.isNotBlank() }
            ?: throw IllegalStateException("Invalid JWT format")
}