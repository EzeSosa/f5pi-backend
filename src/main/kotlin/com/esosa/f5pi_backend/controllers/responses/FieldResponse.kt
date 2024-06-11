package com.esosa.f5pi_backend.controllers.responses

import java.util.UUID

data class FieldResponse(
    val fieldId: UUID,
    val fieldName: String
)