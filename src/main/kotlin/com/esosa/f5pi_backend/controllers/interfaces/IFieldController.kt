package com.esosa.f5pi_backend.controllers.interfaces

import com.esosa.f5pi_backend.controllers.requests.CreateFieldRequest
import com.esosa.f5pi_backend.controllers.requests.UpdateFieldRequest
import com.esosa.f5pi_backend.controllers.responses.FieldResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.UUID

@RequestMapping("/api/v1/fields")
interface IFieldController {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun saveField(@RequestBody @Valid createFieldRequest: CreateFieldRequest): FieldResponse

    @PatchMapping("/{fieldId}")
    @ResponseStatus(HttpStatus.OK)
    fun updateField(@PathVariable fieldId: UUID, @RequestBody @Valid updateFieldRequest: UpdateFieldRequest): FieldResponse

    @DeleteMapping("/{fieldId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteField(@PathVariable fieldId: UUID)
}