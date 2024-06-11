package com.esosa.f5pi_backend.services.implementations

import com.esosa.f5pi_backend.controllers.requests.CreateFieldRequest
import com.esosa.f5pi_backend.controllers.requests.UpdateFieldRequest
import com.esosa.f5pi_backend.controllers.responses.FieldResponse
import com.esosa.f5pi_backend.data.models.Field
import com.esosa.f5pi_backend.data.models.User
import com.esosa.f5pi_backend.data.repositories.IFieldRepository
import com.esosa.f5pi_backend.services.interfaces.IFieldService
import com.esosa.f5pi_backend.services.interfaces.IUserService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class FieldService(
    private val fieldRepository: IFieldRepository,
    private val userService: IUserService
) : IFieldService {

    override fun saveField(createFieldRequest: CreateFieldRequest): FieldResponse =
        with(createFieldRequest) {
            val user = userService.findUserByIdOrThrowException(userId)
            fieldRepository.save(buildField(user))
                .buildFieldResponse()
        }

    override fun updateField(fieldId: UUID, updateFieldRequest: UpdateFieldRequest): FieldResponse =
        findFieldByIdOrThrowException(fieldId).let {field ->
            fieldRepository.save(field).apply { name = updateFieldRequest.name }
                .buildFieldResponse()
        }

    override fun deleteField(fieldId: UUID) {
        ifFieldDoesNotExistThrowException(fieldId)
        fieldRepository.deleteById(fieldId)
    }

    override fun findFieldByIdOrThrowException(fieldId: UUID): Field =
        fieldRepository.findById(fieldId)
            .orElseThrow { ResponseStatusException(HttpStatus.BAD_REQUEST, "Field with id $fieldId does not exist") }

    private fun ifFieldDoesNotExistThrowException(fieldId: UUID) {
        if (!fieldRepository.existsById(fieldId))
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Player with id $fieldId does not exist")
    }

    private fun CreateFieldRequest.buildField(user: User) = Field(name, user)
}