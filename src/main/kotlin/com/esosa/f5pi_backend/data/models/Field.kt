package com.esosa.f5pi_backend.data.models

import com.esosa.f5pi_backend.controllers.responses.FieldResponse
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.time.LocalDateTime
import java.util.UUID

@Entity
data class Field(
    var name: String,

    @ManyToOne
    val user: User,

    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Id
    val id: UUID = UUID.randomUUID()
) {
    fun buildFieldResponse(): FieldResponse = FieldResponse(id, name)
}