package com.esosa.f5pi_backend.exceptions

import java.time.LocalDateTime

data class ExceptionPayload(
    val message: String?,
    val status: Int,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val trace: Array<StackTraceElement?>
)