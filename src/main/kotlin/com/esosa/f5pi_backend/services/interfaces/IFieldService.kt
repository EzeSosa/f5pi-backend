package com.esosa.f5pi_backend.services.interfaces

import com.esosa.f5pi_backend.controllers.requests.CreateFieldRequest
import com.esosa.f5pi_backend.controllers.requests.UpdateFieldRequest
import com.esosa.f5pi_backend.controllers.responses.FieldResponse
import com.esosa.f5pi_backend.data.models.Field
import com.esosa.f5pi_backend.data.models.User
import org.springframework.data.domain.Page
import java.util.UUID

interface IFieldService {
    fun saveField(createFieldRequest: CreateFieldRequest): FieldResponse
    fun updateField(fieldId: UUID, updateFieldRequest: UpdateFieldRequest): FieldResponse
    fun deleteField(fieldId: UUID)
    fun findFieldByIdOrThrowException(fieldId: UUID): Field
    fun getUserFields(user: User, pageNumber: Int, pageSize: Int, sortAttribute: String, sortOrder: String): Page<FieldResponse>
}