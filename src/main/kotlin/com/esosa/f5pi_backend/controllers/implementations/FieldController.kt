package com.esosa.f5pi_backend.controllers.implementations

import com.esosa.f5pi_backend.controllers.interfaces.IFieldController
import com.esosa.f5pi_backend.controllers.requests.CreateFieldRequest
import com.esosa.f5pi_backend.controllers.requests.UpdateFieldRequest
import com.esosa.f5pi_backend.controllers.responses.FieldResponse
import com.esosa.f5pi_backend.services.interfaces.IFieldService
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class FieldController(private val fieldService: IFieldService) : IFieldController {
    override fun saveField(createFieldRequest: CreateFieldRequest): FieldResponse =
        fieldService.saveField(createFieldRequest)

    override fun updateField(fieldId: UUID, updateFieldRequest: UpdateFieldRequest): FieldResponse =
        fieldService.updateField(fieldId, updateFieldRequest)

    override fun deleteField(fieldId: UUID) =
        fieldService.deleteField(fieldId)
}