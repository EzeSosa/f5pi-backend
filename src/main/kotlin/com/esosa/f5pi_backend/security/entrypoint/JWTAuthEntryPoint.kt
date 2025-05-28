package com.esosa.f5pi_backend.security.entrypoint

import com.esosa.f5pi_backend.exceptions.ExceptionPayload
import com.esosa.f5pi_backend.security.utils.Constants.Companion.CLAIMS_JWT_EXCEPTION_MESSAGE
import com.esosa.f5pi_backend.security.utils.Constants.Companion.EXPIRED_JWT_EXCEPTION_MESSAGE
import com.esosa.f5pi_backend.security.utils.Constants.Companion.JWT_EXCEPTION_DEFAULT_MESSAGE
import com.esosa.f5pi_backend.security.utils.Constants.Companion.MISSING_HEADER_EXCEPTION_MESSAGE
import com.esosa.f5pi_backend.security.utils.Constants.Companion.SIGNATURE_EXCEPTION_MESSAGE
import com.esosa.f5pi_backend.security.utils.Constants.Companion.USERNAME_NOT_FOUND_MESSAGE
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.SignatureException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class JWTAuthEntryPoint(private val objectMapper: ObjectMapper) : AuthenticationEntryPoint {

    @Value("\${cors.originPatterns:default}")
    private val corsOriginPatterns: String = ""

    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        buildExceptionPayload(authException?.cause ?: authException!!)
            .also { configureResponse(response, it) }
    }

    private fun configureResponse(response: HttpServletResponse?, payload: ExceptionPayload) {
        response?.apply {
            contentType = APPLICATION_JSON_VALUE
            status = payload.status
            writer.write(objectMapper.writeValueAsString(payload))
            setHeader("Access-Control-Allow-Origin", corsOriginPatterns)
        }
    }

    private fun buildExceptionPayload(throwable: Throwable): ExceptionPayload =
        buildExceptionMessage(throwable).let {
            ExceptionPayload(
                it.first,
                it.second.value(),
                LocalDateTime.now(),
                throwable.stackTrace
            )
        }

    private fun buildExceptionMessage(throwable: Throwable): Pair<String, HttpStatus> {
        return when (throwable) {
            is ExpiredJwtException -> EXPIRED_JWT_EXCEPTION_MESSAGE
            is SignatureException -> SIGNATURE_EXCEPTION_MESSAGE
            is MalformedJwtException -> CLAIMS_JWT_EXCEPTION_MESSAGE
            is IllegalArgumentException -> MISSING_HEADER_EXCEPTION_MESSAGE
            is UsernameNotFoundException -> USERNAME_NOT_FOUND_MESSAGE
            else -> JWT_EXCEPTION_DEFAULT_MESSAGE
        }
    }
}