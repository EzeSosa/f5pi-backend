package com.esosa.f5pi_backend.services.implementations

import com.esosa.f5pi_backend.controllers.requests.CreateFieldRequest
import com.esosa.f5pi_backend.controllers.requests.UpdateFieldRequest
import com.esosa.f5pi_backend.data.models.Field
import com.esosa.f5pi_backend.data.models.User
import com.esosa.f5pi_backend.data.repositories.IFieldRepository
import com.esosa.f5pi_backend.services.interfaces.IUserService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.web.server.ResponseStatusException
import java.util.Optional
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class FieldServiceTest {

    @Mock lateinit var fieldRepository: IFieldRepository
    @Mock lateinit var userService: IUserService

    @InjectMocks
    lateinit var fieldService: FieldService

    object TestFieldRequests {
        val CREATE_REQUEST = CreateFieldRequest(
            name = "Test Field",
            userId = UUID.randomUUID()
        )
        val UPDATE_REQUEST = UpdateFieldRequest(
            name = "Updated Field Name"
        )
    }

    @Test
    fun saveField() {

        val field = Field(
            name = TestFieldRequests.CREATE_REQUEST.name,
            user = User()
        )

        whenever(userService.findUserByIdOrThrowException(TestFieldRequests.CREATE_REQUEST.userId))
            .thenReturn(field.user)
        whenever(fieldRepository.save(any<Field>()))
            .thenReturn(field)

        fieldService.saveField(TestFieldRequests.CREATE_REQUEST).also {
            assertEquals(TestFieldRequests.CREATE_REQUEST.name, it.fieldName)
        }
    }

    @Test
    fun saveFieldWithExistingName() {

        val user = User()

        whenever(userService.findUserByIdOrThrowException(TestFieldRequests.CREATE_REQUEST.userId))
            .thenReturn(user)
        whenever(fieldRepository.existsByNameAndUser(TestFieldRequests.CREATE_REQUEST.name, user))
            .thenReturn(true)

        val exception = assertThrows<ResponseStatusException> {
            fieldService.saveField(TestFieldRequests.CREATE_REQUEST)
        }
        assertEquals("Field name ${TestFieldRequests.CREATE_REQUEST.name} already exists for the user", exception.reason)

    }

    @Test
    fun updateField() {

        val originalField = Field(
            id = UUID.randomUUID(),
            name = "Original Field Name",
            user = User()
        )

        val modifiedField = Field(
            id = originalField.id,
            name = TestFieldRequests.UPDATE_REQUEST.name,
            user = originalField.user
        )

        whenever(fieldRepository.findById(originalField.id))
            .thenReturn(Optional.of(originalField))
        whenever(fieldRepository.save(any<Field>()))
            .thenReturn(modifiedField)

        fieldService.updateField(originalField.id, TestFieldRequests.UPDATE_REQUEST).also {
            assertEquals(TestFieldRequests.UPDATE_REQUEST.name, it.fieldName)
            assertEquals(modifiedField.name, it.fieldName)
            assertEquals(modifiedField.id, it.fieldId)
        }

    }

    @Test
    fun updateFieldWithNonExistingField() {

        val fieldId = UUID.randomUUID()

        whenever(fieldRepository.findById(fieldId))
            .thenReturn(Optional.empty())

        val exception = assertThrows<ResponseStatusException> {
            fieldService.updateField(fieldId, TestFieldRequests.UPDATE_REQUEST)
        }
        assertEquals("Field with id $fieldId does not exist", exception.reason)

    }

}