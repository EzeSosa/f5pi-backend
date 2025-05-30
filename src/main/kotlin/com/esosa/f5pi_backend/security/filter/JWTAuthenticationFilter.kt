package com.esosa.f5pi_backend.security.filter

import com.esosa.f5pi_backend.security.entrypoint.JWTAuthEntryPoint
import com.esosa.f5pi_backend.security.entrypoint.JWTAuthException
import com.esosa.f5pi_backend.security.jwt.IJWTService
import com.esosa.f5pi_backend.security.service.CustomUserDetailsService
import com.esosa.f5pi_backend.security.utils.Constants.Companion.MISSING_HEADER_EXCEPTION_MESSAGE
import com.esosa.f5pi_backend.utils.SWAGGER_URLS
import com.esosa.f5pi_backend.utils.WHITE_LIST_URLS
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.http.HttpHeaders.AUTHORIZATION
import org.springframework.beans.factory.annotation.Qualifier
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
    @Qualifier("customUserDetailsService") private val userDetailsService: CustomUserDetailsService,
    private val jwtService: IJWTService,
    private val pathMatcher: AntPathMatcher,
    private val entryPoint: JWTAuthEntryPoint
) : OncePerRequestFilter() {

    companion object {
        private const val BEARER_PREFIX = "Bearer "
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        runCatching {
            authenticateRequest(request)
            filterChain.doFilter(request, response)
        }.onFailure {
            val authException = it as? AuthenticationException ?: JWTAuthException(it)
            entryPoint.commence(request, response, authException)
        }
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean =
        arrayOf(*WHITE_LIST_URLS, *SWAGGER_URLS).any { pathMatcher.match(it, request.requestURI) }

    private fun authenticateRequest(request: HttpServletRequest) {
        val token = request.getTokenFromAuthHeader()
        val username = runCatchingAuthException { jwtService.extractUsernameFromToken(token) }
        val user = runCatchingAuthException { userDetailsService.loadUserByUsername(username) }

        if (jwtService.isTokenValid(token, username))
            updateContext(user, request)
    }

    private fun HttpServletRequest.getTokenFromAuthHeader(): String =
        getHeader(AUTHORIZATION)
            ?.takeIf { it.startsWith(BEARER_PREFIX) }
            ?.substringAfter(BEARER_PREFIX)
            ?: throw JWTAuthException(
                MISSING_HEADER_EXCEPTION_MESSAGE.first,
                IllegalArgumentException()
            )

    private fun updateContext(user: UserDetails, request: HttpServletRequest) {
        UsernamePasswordAuthenticationToken(user, null, user.authorities)
            .apply { details = WebAuthenticationDetailsSource().buildDetails(request) }
            .also { SecurityContextHolder.getContext().authentication = it }
    }

    private inline fun <T> runCatchingAuthException(block: () -> T): T =
        runCatching { block() }
            .getOrElse { throw JWTAuthException(it.localizedMessage, it) }
}