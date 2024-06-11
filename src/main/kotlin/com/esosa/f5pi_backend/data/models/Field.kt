package com.esosa.f5pi_backend.data.models

import com.esosa.f5pi_backend.controllers.responses.FieldResponse
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.UUID

@Entity
data class Field(
    var name: String,
    val user: User,

    @Id
    val id: UUID = UUID.randomUUID()
) {
    fun buildFieldResponse(): FieldResponse = FieldResponse(id, name)
}