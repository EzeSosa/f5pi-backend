package com.esosa.f5pi_backend.exceptions

import io.jsonwebtoken.JwtException
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ResponseStatusException

@RestControllerAdvice
class ExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatusException(ex: ResponseStatusException): ExceptionPayload =
        ExceptionPayload(
            message = ex.reason,
            status = ex.statusCode.value(),
            trace = ex.stackTrace
        )

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ExceptionPayload =
        ExceptionPayload(
            message = ex.bindingResult.allErrors[0].defaultMessage,
            status = ex.statusCode.value(),
            trace = ex.stackTrace
        )

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(ex: HttpMessageNotReadableException): ExceptionPayload =
        ExceptionPayload(
            message = "There is a problem with the request. Contact an administrator.",
            status = HttpStatus.BAD_REQUEST.value(),
            trace = ex.stackTrace
        )

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JwtException::class)
    fun handleJWTDecodeException(ex: JwtException): ExceptionPayload =
        ExceptionPayload(
            message = "There is a problem with the token. Contact an administrator.",
            status = HttpStatus.UNAUTHORIZED.value(),
            trace = ex.stackTrace
        )

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentialsException(ex: BadCredentialsException): ExceptionPayload =
        ExceptionPayload(
            message = "Username or password incorrect",
            status = HttpStatus.BAD_REQUEST.value(),
            trace = ex.stackTrace
        )

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UsernameNotFoundException::class)
    fun handleUsernameNotFoundException(ex: UsernameNotFoundException): ExceptionPayload =
        ExceptionPayload(
            message = "Username does not exist.",
            status = HttpStatus.BAD_REQUEST.value(),
            trace = ex.stackTrace
        )
}