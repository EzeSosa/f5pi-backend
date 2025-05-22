package com.esosa.f5pi_backend.security.filter

import com.esosa.f5pi_backend.security.entrypoint.JWTAuthEntryPoint
import com.esosa.f5pi_backend.security.entrypoint.JWTAuthException
import com.esosa.f5pi_backend.security.jwt.JWTService
import com.esosa.f5pi_backend.security.service.CustomUserDetailsService
import com.esosa.f5pi_backend.security.utils.Constants.Companion.MISSING_HEADER_EXCEPTION_MESSAGE
import com.esosa.f5pi_backend.utils.SWAGGER_URLS
import com.esosa.f5pi_backend.utils.WHITE_LIST_URLS
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JWTAuthenticationFilter(
    private val userDetailsService: CustomUserDetailsService,
    private val jwtService: JWTService,
    private val pathMatcher: AntPathMatcher,
    private val entryPoint: JWTAuthEntryPoint
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        runCatching {
            authenticateRequest(request)
            filterChain.doFilter(request, response)
        }.onFailure {
            val authException = it as? AuthenticationException
                ?: JWTAuthException("Authentication failed", it)
            entryPoint.commence(request, response, authException)
        }
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean =
        arrayOf(*WHITE_LIST_URLS, *SWAGGER_URLS).any { pathMatcher.match(it, request.requestURI) }

    private fun authenticateRequest(request: HttpServletRequest) {
        val authHeader = request.getValidAuthHeader()
        val token = authHeader.extractToken()
        val username = runCatchingAuthException { jwtService.extractUsernameFromToken(token) }
        val user = runCatchingAuthException { userDetailsService.loadUserByUsername(username) }

        if (jwtService.isTokenValid(token, user))
            updateContext(user, request)
    }

    private fun HttpServletRequest.getValidAuthHeader(): String =
        getHeader(AUTHORIZATION)
            ?.takeIf { it.startsWith("Bearer ") }
            ?: throw JWTAuthException(
                MISSING_HEADER_EXCEPTION_MESSAGE.first,
                IllegalArgumentException("Authorization header is missing or invalid")
            )

    private fun String.extractToken(): String =
        substringAfter("Bearer ").takeIf { it.isNotBlank() }
            ?: throw IllegalStateException("Invalid JWT format")

    private fun updateContext(user: UserDetails, request: HttpServletRequest) {
        UsernamePasswordAuthenticationToken(user, null, user.authorities)
            .apply { details = WebAuthenticationDetailsSource().buildDetails(request) }
            .also { SecurityContextHolder.getContext().authentication = it }
    }

    private inline fun <T> runCatchingAuthException(block: () -> T): T =
        runCatching { block() }
            .getOrElse { throw JWTAuthException(it.localizedMessage, it) }
}